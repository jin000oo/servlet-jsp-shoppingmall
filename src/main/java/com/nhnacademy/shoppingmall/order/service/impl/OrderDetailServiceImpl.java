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

import com.nhnacademy.shoppingmall.order.domain.OrderDetail;
import com.nhnacademy.shoppingmall.order.repository.OrderDetailRepository;
import com.nhnacademy.shoppingmall.order.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public void saveOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null) {
            log.warn("[OrderDetailServiceImpl] order detail is null");
            throw new IllegalArgumentException("[OrderDetailServiceImpl] order detail is null");
        }

        // 주문 내역 저장
        orderDetailRepository.save(orderDetail);
    }

}
