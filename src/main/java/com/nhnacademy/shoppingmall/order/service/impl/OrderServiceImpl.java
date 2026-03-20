/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2026. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.shoppingmall.order.service.impl;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.order.domain.Order;
import com.nhnacademy.shoppingmall.order.domain.OrderDetail;
import com.nhnacademy.shoppingmall.order.exception.InsufficientAmountException;
import com.nhnacademy.shoppingmall.order.exception.InsufficientQuantityException;
import com.nhnacademy.shoppingmall.order.repository.OrderDetailRepository;
import com.nhnacademy.shoppingmall.order.repository.OrderRepository;
import com.nhnacademy.shoppingmall.order.service.OrderService;
import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.service.PointService;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.exception.ProductNotFoundException;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.thread.channel.RequestChannel;
import com.nhnacademy.shoppingmall.thread.request.impl.PointChannelRequest;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final ProductService productService;
    private final PointService pointService;
    private final RequestChannel requestChannel;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository,
                            UserRepository userRepository, ProductRepository productRepository,
                            ProductService productService, PointService pointService, RequestChannel requestChannel) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;

        this.productService = productService;
        this.pointService = pointService;
        this.requestChannel = requestChannel;
    }

    @Override
    public void order(Order order, List<OrderDetail> orderDetails) {
        if (order == null) {
            log.warn("[OrderServiceImpl] order is null");
            throw new IllegalArgumentException("[OrderServiceImpl] order is null");
        }

        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new UserNotFoundException(order.getUserId()));

        // 잔액 검증
        if (user.getUserPoint() < order.getTotalAmount()) {
            throw new InsufficientAmountException(order.getUserId());
        }

        // 주문된 상품 ID (DB 조회)
        List<String> productIds = orderDetails.stream()
                .map(OrderDetail::getProductId)
                .toList();

        Map<String, Product> productMap = productRepository.findByIds(productIds).stream()
                .collect(Collectors.toMap(Product::getProductId, p -> p));

        // 재고 검증
        for (OrderDetail orderDetail : orderDetails) {
            Product product = productMap.get(orderDetail.getProductId());

            if (product == null) {
                throw new ProductNotFoundException(orderDetail.getProductId());
            }

            if (product.getStock() < orderDetail.getQuantity()) {
                throw new InsufficientQuantityException(orderDetail.getProductId());
            }
        }

        // 재고 차감: products 테이블 수정
        for (OrderDetail orderDetail : orderDetails) {
            Product product = productMap.get(orderDetail.getProductId());
            product.setStock(product.getStock() - orderDetail.getQuantity());
            productService.updateProduct(product);
        }

        // 주문 및 주문 상세 내역 저장
        orderRepository.save(order);

        for (OrderDetail orderDetail : orderDetails) {
            orderDetailRepository.save(orderDetail);
        }

        // 포인트 차감 내역 저장
        Point usePoint = new Point(
                UUID.randomUUID().toString(),
                order.getUserId(),
                order.getOrderId(),
                -order.getTotalAmount(),
                "주문 결제 사용"
        );
        pointService.savePoint(usePoint);

        // 총액의 10% 포인트 적립
        int earnPoint = (int) (order.getTotalAmount() * 0.1);

        Point point = new Point(
                UUID.randomUUID().toString(),
                order.getUserId(),
                order.getOrderId(),
                earnPoint,
                "주문 적립"
        );

        // 포인트 적립 요청 생성 (비동기)
        PointChannelRequest pointChannelRequest = new PointChannelRequest(pointService, point);

        try {
            // 요청 채널에 포인트 적립 요청 등록
            requestChannel.addRequest(pointChannelRequest);

        } catch (InterruptedException e) {
            log.error("fail to add point request channel - orderId: {}", order.getOrderId(), e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Page<Order> getOrderList(String userId, int page) {
        validateId(userId);

        int pageSize = Page.DEFAULT_PAGE_SIZE;
        int offset = (page - 1) * pageSize;

        List<Order> orderList = orderRepository.findByUserId(userId, pageSize, offset);

        int totalCount = orderRepository.countByUserId(userId);

        return new Page<>(orderList, totalCount);
    }

    @Override
    public List<OrderDetail> getOrderDetails(String orderId) {
        validateId(orderId);

        return orderDetailRepository.findByOrderId(orderId);
    }

    private void validateId(String id) {
        if (id == null || id.isBlank()) {
            log.warn("[OrderServiceImpl] id is null or blank");
            throw new IllegalArgumentException("[OrderServiceImpl] id is null or blank");
        }
    }

}
