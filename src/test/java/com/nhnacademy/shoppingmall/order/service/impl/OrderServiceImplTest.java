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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.thread.channel.RequestChannel;
import com.nhnacademy.shoppingmall.thread.request.impl.PointChannelRequest;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    OrderDetailRepository orderDetailRepository = Mockito.mock(OrderDetailRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    ProductRepository productRepository = Mockito.mock(ProductRepository.class);

    ProductService productService = Mockito.mock(ProductService.class);
    PointService pointService = Mockito.mock(PointService.class);
    RequestChannel requestChannel = Mockito.mock(RequestChannel.class);

    OrderService orderService = new OrderServiceImpl(orderRepository, orderDetailRepository,
            userRepository, productRepository,
            productService, pointService, requestChannel);

    User testUser = new User(
            "test-user",
            "nhn아카데미",
            "nhnacademy-test-password",
            "19900505",
            User.Auth.ROLE_USER,
            100_0000,
            LocalDateTime.now(),
            null
    );

    Order testOrder = new Order(
            "test-order",
            "test-user",
            100_000
    );

    OrderDetail testOrderDetail = new OrderDetail(
            "test-order-detail",
            "test-order",
            "test-product",
            100
    );

    Product testProduct = new Product(
            "test-product",
            "test-product_name",
            1_000,
            1000,
            List.of("test-category"));

    @Test
    @DisplayName("주문 성공 - 잔액 및 재고 차감, 비동기 큐 전송")
    void order() throws InterruptedException {
        List<Product> productIds = new ArrayList<>();
        productIds.add(testProduct);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(productRepository.findByIds(any())).thenReturn(productIds);

        orderService.order(testOrder, List.of(testOrderDetail));

        verify(productService, times(1)).updateProduct(testProduct);
        verify(orderRepository, times(1)).save(testOrder);
        verify(orderDetailRepository, times(1)).save(testOrderDetail);
        verify(pointService, times(1)).savePoint(any(Point.class));
        verify(requestChannel, times(1)).addRequest(any(PointChannelRequest.class));
    }

    @Test
    @DisplayName("주문 실패 - 잔액 부족")
    void order_fail1() throws InterruptedException {
        testUser.setUserPoint(0);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));

        Assertions.assertThrows(InsufficientAmountException.class, () ->
                orderService.order(testOrder, List.of(testOrderDetail)));

        verify(userRepository, never()).update(any(User.class));
        verify(productRepository, never()).findById(anyString());
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderDetailRepository, never()).save(any(OrderDetail.class));
        verify(requestChannel, never()).addRequest(any(PointChannelRequest.class));
    }

    @Test
    @DisplayName("주문 실패 - 재고 부족")
    void order_fail2() throws InterruptedException {
        testProduct.setStock(1);

        List<Product> productIds = new ArrayList<>();
        productIds.add(testProduct);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(testUser));
        when(productRepository.findByIds(any())).thenReturn(productIds);

        Assertions.assertThrows(InsufficientQuantityException.class, () ->
                orderService.order(testOrder, List.of(testOrderDetail)));

        verify(userRepository, never()).update(any(User.class));
        verify(orderRepository, never()).save(any(Order.class));
        verify(orderDetailRepository, never()).save(any(OrderDetail.class));
        verify(requestChannel, never()).addRequest(any(PointChannelRequest.class));
    }

    @Test
    @DisplayName("주문 내역 조회")
    void getOrderList() {
        orderService.getOrderList(testUser.getUserId(), 1);

        verify(orderRepository, times(1)).findByUserId(anyString(), anyInt(), anyInt());
        verify(orderRepository, times(1)).countByUserId(anyString());
    }

    @Test
    @DisplayName("주문 내역 조회 실패 - id가 null이거나 비어있음")
    void getOrderList_fail() {
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        orderService.getOrderList(null, 1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        orderService.getOrderList("", 1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        orderService.getOrderList(" ", 1))
        );
    }

    @Test
    @DisplayName("주문 상세 조회")
    void getOrderDetails() {
        orderService.getOrderDetails(testOrder.getOrderId());

        verify(orderDetailRepository, times(1)).findByOrderId(testOrder.getOrderId());
    }

    @Test
    @DisplayName("주문 상세 조회 실패 - id가 null이거나 비어있음")
    void getOrderDetails_fail() {
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        orderService.getOrderDetails(null)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        orderService.getOrderDetails("")),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        orderService.getOrderDetails(" "))
        );
    }

}
