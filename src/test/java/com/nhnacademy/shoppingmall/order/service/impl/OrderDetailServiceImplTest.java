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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.nhnacademy.shoppingmall.order.domain.OrderDetail;
import com.nhnacademy.shoppingmall.order.repository.OrderDetailRepository;
import com.nhnacademy.shoppingmall.order.service.OrderDetailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderDetailServiceImplTest {

    OrderDetailRepository orderDetailRepository = Mockito.mock(OrderDetailRepository.class);
    OrderDetailService orderDetailService = new OrderDetailServiceImpl(orderDetailRepository);

    OrderDetail testOrderDetail = new OrderDetail(
            "test-order-detail",
            "test-order",
            "test-product",
            100
    );

    @Test
    @DisplayName("주문 내역 생성")
    void saveOrderDetail() {
        orderDetailService.saveOrderDetail(testOrderDetail);

        verify(orderDetailRepository, times(1)).save(testOrderDetail);
    }

    @Test
    @DisplayName("주문 내역 생성 실패 - 객체가 null")
    void saveOrderDetail_fail() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                orderDetailService.saveOrderDetail(null));

        verify(orderDetailRepository, never()).save(any(OrderDetail.class));
    }

}
