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

package com.nhnacademy.shoppingmall.point.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.order.domain.Order;
import com.nhnacademy.shoppingmall.order.repository.OrderRepository;
import com.nhnacademy.shoppingmall.order.repository.impl.OrderRepositoryImpl;
import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.repository.PointRepository;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PointRepositoryImplTest {

    PointRepository pointRepository = new PointRepositoryImpl();
    UserRepository userRepository = new UserRepositoryImpl();
    OrderRepository orderRepository = new OrderRepositoryImpl();

    Point testPoint;
    User testUser;
    Order testOrder;

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

        testPoint = new Point(
                "test-point",
                "test-user",
                "test-order",
                10_000,
                "test-reason"
        );

        userRepository.save(testUser);
        orderRepository.save(testOrder);
    }

    @AfterEach
    void tearDown() {
        DbConnectionThreadLocal.setSqlError(true);
        DbConnectionThreadLocal.reset();
    }

    @Test
    @DisplayName("포인트 적립/차감 내역 저장")
    void save() {
        int result = pointRepository.save(testPoint);

        Assertions.assertEquals(1, result);
    }

    @Test
    @DisplayName("포인트 내역 조회")
    void findByUserId() {
        pointRepository.save(testPoint);
        
        List<Point> pointList = pointRepository.findByUserId(testUser.getUserId());

        Assertions.assertEquals(1, pointList.size());
    }

}
