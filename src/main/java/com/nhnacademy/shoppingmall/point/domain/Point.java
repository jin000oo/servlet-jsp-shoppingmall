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

package com.nhnacademy.shoppingmall.point.domain;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class Point {

    private String pointId;
    private String userId;
    private String orderId;
    private int amount;
    private String reason;
    private LocalDateTime createdAt;

    public Point(String pointId, String userId, String orderId, int amount, String reason) {
        this.pointId = pointId;
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }

    public Point(String pointId, String userId, String orderId, int amount, String reason, LocalDateTime createdAt) {
        this.pointId = pointId;
        this.userId = userId;
        this.orderId = orderId;
        this.amount = amount;
        this.reason = reason;
        this.createdAt = createdAt;
    }

}
