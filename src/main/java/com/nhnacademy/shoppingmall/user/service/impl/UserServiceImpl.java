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

package com.nhnacademy.shoppingmall.user.service.impl;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.repository.impl.PointRepositoryImpl;
import com.nhnacademy.shoppingmall.point.service.PointService;
import com.nhnacademy.shoppingmall.point.service.impl.PointServiceImpl;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.exception.UserAlreadyExistsException;
import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import com.nhnacademy.shoppingmall.user.service.UserService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String userId) {
        //todo#4-1 회원조회
        validateUserId(userId);

        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public void saveUser(User user) {
        //todo#4-2 회원등록
        validateUser(user);

        if (isExistUser(user.getUserId())) {
            throw new UserAlreadyExistsException(user.getUserId());
        }

        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        //todo#4-3 회원수정
        validateUser(user);

        if (!isExistUser(user.getUserId())) {
            throw new UserNotFoundException(user.getUserId());
        }

        userRepository.update(user);
    }

    @Override
    public void deleteUser(String userId) {
        //todo#4-4 회원삭제
        validateUserId(userId);

        if (!isExistUser(userId)) {
            throw new UserNotFoundException(userId);
        }

        userRepository.deleteByUserId(userId);
    }

    @Override
    public User doLogin(String userId, String userPassword) {
        //todo#4-5 로그인 구현, userId, userPassword로 일치하는 회원 조회
        validateUserId(userId);

        if (userPassword == null || userPassword.isBlank()) {
            log.warn("[UserServiceImpl] user password is null or blank");
            throw new IllegalArgumentException("[UserServiceImpl] user password is null or blank");
        }

        User user = userRepository.findByUserIdAndUserPassword(userId, userPassword)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // 매일 첫 로그인 시 1만 포인트 적립
        LocalDateTime lastLoginAt = user.getLatestLoginAt();
        LocalDate today = LocalDate.now();

        if (lastLoginAt == null || !lastLoginAt.toLocalDate().isEqual(today)) {
            PointService pointService = new PointServiceImpl(new PointRepositoryImpl(), this.userRepository);

            Point loginPoint = new Point(
                    UUID.randomUUID().toString(),
                    user.getUserId(),
                    null,
                    10_000,
                    "매일 첫 로그인 시 적립"
            );
            pointService.savePoint(loginPoint);
        }

        userRepository.updateLatestLoginAtByUserId(userId, LocalDateTime.now());

        return userRepository.findById(userId)
                .orElse(user);
    }

    @Override
    public Page<User> getUsers(int page, int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("page or pageSize must be a positive number");
        }
        return userRepository.findAll(page, pageSize);
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            log.warn("[UserServiceImpl] user id is null or blank");
            throw new IllegalArgumentException("[UserServiceImpl] user id is null or blank");
        }
    }

    private void validateUser(User user) {
        if (user == null) {
            log.warn("[UserServiceImpl] user is null");
            throw new IllegalArgumentException("[UserServiceImpl] user is null");
        }
    }

    private boolean isExistUser(String userId) {
        return userRepository.countByUserId(userId) > 0;
    }
}
