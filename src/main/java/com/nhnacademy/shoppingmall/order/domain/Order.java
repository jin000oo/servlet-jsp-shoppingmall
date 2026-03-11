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

package com.nhnacademy.shoppingmall.order.domain;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@Getter
@ToString
public class Order {

    private String orderId;
    private String userId;
    private int totalAmount;
    private int earnPoint;
    private LocalDateTime createdAt;

    public Order(String orderId, String userId, int totalAmount, int earnPoint) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.earnPoint = earnPoint;
        this.createdAt = LocalDateTime.now();
    }

    public Order(String orderId, String userId, int totalAmount, int earnPoint, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.earnPoint = earnPoint;
        this.createdAt = createdAt;
    }

}
