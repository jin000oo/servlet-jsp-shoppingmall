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

package com.nhnacademy.shoppingmall.order.repository;

import com.nhnacademy.shoppingmall.order.domain.Order;
import java.util.List;

public interface OrderRepository {

    // 주문 저장
    int save(Order order);

    // 주문 내역 조회
    List<Order> findByUserId(String userId, int limit, int offset);

    // 총 데이터 개수 조회
    int countByUserId(String userId);

    // 회원 탈퇴 시 orders 테이블 user_id 업데이트
    int updateUserId(String oldUserId, String newUserId);

}
