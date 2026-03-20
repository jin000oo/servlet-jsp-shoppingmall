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

package com.nhnacademy.shoppingmall.user.service;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.domain.User;

public interface UserService {

    String CONTEXT_USER_SERVICE_NAME = "CONTEXT_USER_SERVICE";

    // 특정 회원 조회
    User getUser(String userId);

    // 회원가입
    void saveUser(User user);

    // 회원 정보 수정
    void updateUser(User user);

    // 회원 탈퇴
    void deleteUser(String userId);

    // 로그인
    User doLogin(String userId, String userPassword);

    // 전체 회원 조회
    Page<User> getUsers(int page, int pageSize);

}
