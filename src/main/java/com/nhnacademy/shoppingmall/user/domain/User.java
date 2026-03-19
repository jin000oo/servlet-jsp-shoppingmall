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

package com.nhnacademy.shoppingmall.user.domain;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class User {

    public enum Auth {
        ROLE_ADMIN, ROLE_USER
    }

    private String userId;
    private String userName;
    private String userPassword;
    private String userBirth;
    private Auth userAuth;
    private int userPoint;
    private LocalDateTime createdAt;
    private LocalDateTime latestLoginAt;

    public User(String userId, String userName, String userPassword, String userBirth, Auth userAuth, int userPoint,
                LocalDateTime createdAt, LocalDateTime latestLoginAt) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userBirth = userBirth;
        this.userAuth = userAuth;
        this.userPoint = userPoint;
        this.createdAt = createdAt;
        this.latestLoginAt = latestLoginAt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public void setUserAuth(Auth userAuth) {
        this.userAuth = userAuth;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }

}
