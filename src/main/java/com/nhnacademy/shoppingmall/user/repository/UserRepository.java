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

package com.nhnacademy.shoppingmall.user.repository;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.domain.User;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository {

    // 로그인 (user_id와 user_password가 일치하는 회원 찾기)
    Optional<User> findByUserIdAndUserPassword(String userId, String userPassword);

    // 특정 회원 조회
    Optional<User> findById(String userId);

    // 회원가입
    int save(User user);

    // 회원 탈퇴
    int deleteByUserId(String userId);

    // 회원 정보 수정
    int update(User user);

    // 마지막 로그인 일시 최신화
    int updateLatestLoginAtByUserId(String userId, LocalDateTime latestLoginAt);

    // 특정 회원 수 조회 (중복 방지)
    int countByUserId(String userId);

    // 전체 회원 조회
    Page<User> findAll(int page, int pageSize);

}
