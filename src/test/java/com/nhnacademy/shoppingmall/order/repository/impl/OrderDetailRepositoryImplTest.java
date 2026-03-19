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

package com.nhnacademy.shoppingmall.order.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.order.domain.Order;
import com.nhnacademy.shoppingmall.order.domain.OrderDetail;
import com.nhnacademy.shoppingmall.order.repository.OrderDetailRepository;
import com.nhnacademy.shoppingmall.order.repository.OrderRepository;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.repository.impl.CategoryRepositoryImpl;
import com.nhnacademy.shoppingmall.product.repository.impl.ProductRepositoryImpl;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderDetailRepositoryImplTest {

    OrderDetailRepository orderDetailRepository = new OrderDetailRepositoryImpl();
    OrderRepository orderRepository = new OrderRepositoryImpl();
    UserRepository userRepository = new UserRepositoryImpl();
    ProductRepository productRepository = new ProductRepositoryImpl();
    CategoryRepository categoryRepository = new CategoryRepositoryImpl();

    OrderDetail testOrderDetail;
    Order testOrder;
    User testUser;
    Product testProduct;
    Category testCategory;

    @BeforeEach
    void setUp() {
        DbConnectionThreadLocal.initialize();

        testUser = new User(
                "test-user",
                "nhn아카데미",
                "nhnacademy-test-password",
                "19900505",
                User.Auth.ROLE_USER,
                100_0000,
                LocalDateTime.now(),
                null
        );

        testOrder = new Order(
                "test-order",
                "test-user",
                10_000
        );

        testCategory = new Category(
                "test-category",
                "test-category-name",
                1
        );

        List<String> categories = new ArrayList<>();
        categories.add("test-category");

        testProduct = new Product(
                "test-product",
                "test-product-name",
                10_000,
                500,
                categories
        );

        testOrderDetail = new OrderDetail(
                "test-order-detail",
                "test-order",
                "test-product",
                10
        );

        userRepository.save(testUser);
        categoryRepository.save(testCategory);
        productRepository.save(testProduct);
        orderRepository.save(testOrder);
    }

    @AfterEach
    void tearDown() {
        DbConnectionThreadLocal.setSqlError(true);
        DbConnectionThreadLocal.reset();
    }

    @Test
    @DisplayName("주문 내역 저장")
    void save() {
        int result = orderDetailRepository.save(testOrderDetail);

        Assertions.assertEquals(1, result);
    }

    @Test
    @DisplayName("주문 상세 조회")
    void findByOrderId() {
        orderDetailRepository.save(testOrderDetail);

        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(testOrder.getOrderId());

        Assertions.assertEquals(1, orderDetailList.size());
    }

}
