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

import com.nhnacademy.shoppingmall.common.mvc.annotation.Service;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.repository.PointRepository;
import com.nhnacademy.shoppingmall.point.service.PointService;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(PointService.CONTEXT_POINT_SERVICE_NAME)
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    public PointServiceImpl(PointRepository pointRepository, UserRepository userRepository) {
        this.pointRepository = pointRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void savePoint(Point point) {
        if (point == null) {
            log.warn("[PointServiceImpl] point is null");
            throw new IllegalArgumentException("[PointServiceImpl] point is null");
        }

        // 해당하는 회원 조회
        User user = userRepository.findById(point.getUserId())
                .orElseThrow(() -> new UserNotFoundException(point.getUserId()));

        // 포인트 내역 저장
        pointRepository.save(point);

        // 포인트 적립: users 테이블 수정
        user.setUserPoint(user.getUserPoint() + point.getAmount());
        userRepository.update(user);
    }

    @Override
    public Page<Point> getPointList(String userId, int page) {
        if (userId == null || userId.isBlank()) {
            log.warn("[PointServiceImpl] user id is null or blank");
            throw new IllegalArgumentException("[PointServiceImpl] user id is null or blank");
        }

        int pageSize = Page.DEFAULT_PAGE_SIZE;
        int offset = (page - 1) * pageSize;

        List<Point> pointList = pointRepository.findByUserId(userId, pageSize, offset);

        int totalCount = pointRepository.countByUserId(userId);

        return new Page<>(pointList, totalCount);
    }

}
