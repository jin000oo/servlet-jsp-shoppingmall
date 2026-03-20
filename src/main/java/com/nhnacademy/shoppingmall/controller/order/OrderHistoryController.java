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

import com.nhnacademy.shoppingmall.cart.repository.impl.CartRepositoryImpl;
import com.nhnacademy.shoppingmall.cart.service.CartService;
import com.nhnacademy.shoppingmall.cart.service.impl.CartServiceImpl;
import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.order.domain.Order;
import com.nhnacademy.shoppingmall.order.domain.OrderDetail;
import com.nhnacademy.shoppingmall.order.repository.OrderDetailRepository;
import com.nhnacademy.shoppingmall.order.repository.OrderRepository;
import com.nhnacademy.shoppingmall.order.repository.impl.OrderDetailRepositoryImpl;
import com.nhnacademy.shoppingmall.order.repository.impl.OrderRepositoryImpl;
import com.nhnacademy.shoppingmall.order.service.OrderService;
import com.nhnacademy.shoppingmall.order.service.impl.OrderServiceImpl;
import com.nhnacademy.shoppingmall.point.repository.impl.PointRepositoryImpl;
import com.nhnacademy.shoppingmall.point.service.PointService;
import com.nhnacademy.shoppingmall.point.service.impl.PointServiceImpl;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.repository.impl.CategoryRepositoryImpl;
import com.nhnacademy.shoppingmall.product.repository.impl.ProductRepositoryImpl;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.product.service.impl.ProductServiceImpl;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/order/history.do")
public class OrderHistoryController implements BaseController {

    private OrderRepository orderRepository = new OrderRepositoryImpl();
    private OrderDetailRepository orderDetailRepository = new OrderDetailRepositoryImpl();
    private UserRepository userRepository = new UserRepositoryImpl();
    private ProductRepository productRepository = new ProductRepositoryImpl();

    private ProductService productService =
            new ProductServiceImpl(new ProductRepositoryImpl(), new CategoryRepositoryImpl());
    private PointService pointService = new PointServiceImpl(new PointRepositoryImpl(), userRepository);
    private CartService cartService = new CartServiceImpl(new CartRepositoryImpl());

    private OrderService orderService = new OrderServiceImpl(orderRepository, orderDetailRepository,
            userRepository, productRepository, productService, pointService, null);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            return "redirect:/login.do";
        }

        int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));

        Page<Order> orderPage = orderService.getOrderList(user.getUserId(), page);
        req.setAttribute("orderList", orderPage.getContent());

        long totalCount = orderPage.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / Page.DEFAULT_PAGE_SIZE);

        if (totalPages == 0) {
            totalPages = 1;
        }

        Map<String, List<OrderDetail>> orderDetailMap = new HashMap<>();
        Map<String, Product> productMap = new HashMap<>();

        for (Order order : orderPage.getContent()) {
            List<OrderDetail> details = orderService.getOrderDetails(order.getOrderId());
            orderDetailMap.put(order.getOrderId(), details);

            for (OrderDetail detail : details) {
                if (!productMap.containsKey(detail.getProductId())) {
                    Product product = productService.getProduct(detail.getProductId());
                    productMap.put(detail.getProductId(), product);
                }
            }
        }

        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("orderDetailMap", orderDetailMap);
        req.setAttribute("productMap", productMap);

        return "shop/order/order_history";
    }

}
