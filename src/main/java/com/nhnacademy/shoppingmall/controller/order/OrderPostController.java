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

package com.nhnacademy.shoppingmall.controller.order;

import com.nhnacademy.shoppingmall.cart.domain.Cart;
import com.nhnacademy.shoppingmall.cart.repository.impl.CartRepositoryImpl;
import com.nhnacademy.shoppingmall.cart.service.CartService;
import com.nhnacademy.shoppingmall.cart.service.impl.CartServiceImpl;
import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.order.domain.Order;
import com.nhnacademy.shoppingmall.order.domain.OrderDetail;
import com.nhnacademy.shoppingmall.order.exception.InsufficientAmountException;
import com.nhnacademy.shoppingmall.order.exception.InsufficientQuantityException;
import com.nhnacademy.shoppingmall.order.repository.OrderDetailRepository;
import com.nhnacademy.shoppingmall.order.repository.OrderRepository;
import com.nhnacademy.shoppingmall.order.repository.impl.OrderDetailRepositoryImpl;
import com.nhnacademy.shoppingmall.order.repository.impl.OrderRepositoryImpl;
import com.nhnacademy.shoppingmall.order.service.OrderService;
import com.nhnacademy.shoppingmall.order.service.impl.OrderServiceImpl;
import com.nhnacademy.shoppingmall.point.repository.impl.PointRepositoryImpl;
import com.nhnacademy.shoppingmall.point.service.PointService;
import com.nhnacademy.shoppingmall.point.service.impl.PointServiceImpl;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.repository.impl.CategoryRepositoryImpl;
import com.nhnacademy.shoppingmall.product.repository.impl.ProductRepositoryImpl;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.product.service.impl.ProductServiceImpl;
import com.nhnacademy.shoppingmall.thread.channel.RequestChannel;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.POST, value = "/order.do")
public class OrderPostController implements BaseController {

    private OrderRepository orderRepository = new OrderRepositoryImpl();
    private OrderDetailRepository orderDetailRepository = new OrderDetailRepositoryImpl();
    private UserRepository userRepository = new UserRepositoryImpl();
    private ProductRepository productRepository = new ProductRepositoryImpl();

    private ProductService productService =
            new ProductServiceImpl(new ProductRepositoryImpl(), new CategoryRepositoryImpl());
    private PointService pointService = new PointServiceImpl(new PointRepositoryImpl(), userRepository);
    private CartService cartService = new CartServiceImpl(new CartRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        RequestChannel requestChannel = (RequestChannel) req.getServletContext().getAttribute("requestChannel");

        OrderService orderService = new OrderServiceImpl(orderRepository, orderDetailRepository,
                userRepository, productRepository, productService, pointService, requestChannel);

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            return "redirect:/login.do";
        }

        try {
            int totalAmount = Integer.parseInt(req.getParameter("totalAmount"));

            List<Cart> cartList = cartService.getCartList(user.getUserId());

            if (cartList == null || cartList.isEmpty()) {
                return "redirect:/cart.do";
            }

            String orderId = UUID.randomUUID().toString();

            Order order = new Order(orderId, user.getUserId(), totalAmount);

            List<OrderDetail> orderDetails = new ArrayList<>();

            for (Cart cart : cartList) {
                OrderDetail orderDetail = new OrderDetail(
                        UUID.randomUUID().toString(),
                        orderId,
                        cart.getProductId(),
                        cart.getQuantity()
                );

                orderDetails.add(orderDetail);
            }

            orderService.order(order, orderDetails);

            // 주문 후 장바구니 목록 삭제
            for (Cart cart : cartList) {
                cartService.deleteCart(user.getUserId(), cart.getProductId());
            }

            // TODO: 추후 결제 성공 시 주문 내역을 보여주거나 결제 완료 됐다는 창으로 redirect하는게 UX적으로 더 좋아보임..
            return "redirect:/index.do";

        } catch (InsufficientAmountException e) {
            req.setAttribute("errorMessage", "포인트 잔액이 부족합니다.");

            return "shop/order/order";

        } catch (InsufficientQuantityException e) {
            req.setAttribute("errorMessage", "일부 상품의 재고가 부족합니다.");

            return "shop/order/order";

        } catch (Exception e) {
            req.setAttribute("errorMessage", "주문 처리 중 시스템 오류가 발생했습니다.");
            
            return "shop/order/order";
        }
    }

}
