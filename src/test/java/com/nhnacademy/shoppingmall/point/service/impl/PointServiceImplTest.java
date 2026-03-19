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

package com.nhnacademy.shoppingmall.point.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.repository.PointRepository;
import com.nhnacademy.shoppingmall.point.service.PointService;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PointServiceImplTest {

    PointRepository pointRepository = Mockito.mock(PointRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    PointService pointService = new PointServiceImpl(pointRepository, userRepository);

    Point testPoint = new Point(
            "test-point",
            "test-user",
            "test-order",
            10_000,
            "test-reason"
    );

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

    @Test
    @DisplayName("포인트 내역 저장 및 users 테이블 필드 업데이트")
    void savePoint() {
        when(userRepository.findById(testPoint.getUserId())).thenReturn(Optional.of(testUser));

        pointService.savePoint(testPoint);

        verify(pointRepository, times(1)).save(testPoint);
        verify(userRepository, times(1)).findById(testUser.getUserId());
        verify(userRepository, times(1)).update(testUser);

        Assertions.assertEquals(101_0000, testUser.getUserPoint());
    }

    @Test
    @DisplayName("포인트 내역 저장 실패 - 객체가 null")
    void savePoint_fail1() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                pointService.savePoint(null));

        verify(pointRepository, never()).save(any(Point.class));
        verify(userRepository, never()).findById(anyString());
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    @DisplayName("포인트 내역 저장 실패 - 찾을 수 없는 유저")
    void savePoint_fail2() {
        when(userRepository.findById(testUser.getUserId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->
                pointService.savePoint(testPoint));

        verify(userRepository, times(1)).findById(testUser.getUserId());
        verify(pointRepository, never()).save(any(Point.class));
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    @DisplayName("포인트 내역 조회")
    void getPointList() {
        pointService.getPointList(testUser.getUserId(), 1);

        verify(pointRepository, times(1)).findByUserId(testUser.getUserId(), 10, 0);
    }

    @Test
    @DisplayName("포인트 내역 조회 실패 - id가 null이거나 비어있음")
    void getPointList_fail() {
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        pointService.getPointList(null, 1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        pointService.getPointList("", 1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        pointService.getPointList(" ", 1))
        );
    }

}
