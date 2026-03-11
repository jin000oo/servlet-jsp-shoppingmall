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

package com.nhnacademy.shoppingmall.thread.request.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.service.PointService;
import com.nhnacademy.shoppingmall.thread.request.ChannelRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PointChannelRequest extends ChannelRequest {

    private final PointService pointService;
    private final Point point;

    public PointChannelRequest(PointService pointService, Point point) {
        this.pointService = pointService;
        this.point = point;
    }

    @Override
    public void execute() {
        DbConnectionThreadLocal.initialize();
        //todo#14-5 포인트 적립구현, connection은 point적립이 완료되면 반납합니다.
        log.debug("pointChannel execute");

        try {
            pointService.savePoint(point);
            log.debug("pointChannel execute success: {}", point);

        } catch (Exception e) {
            log.error("pointChannel execute fail: {}", e.getMessage(), e);
            DbConnectionThreadLocal.setSqlError(true);

        } finally {
            DbConnectionThreadLocal.reset();
        }
    }

}
