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

package com.nhnacademy.shoppingmall.point.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.repository.PointRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PointRepositoryImpl implements PointRepository {

    @Override
    public int save(Point point) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                INSERT INTO points (point_id, user_id, order_id, amount, reason, created_at) 
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, point.getPointId());
            psmt.setString(2, point.getUserId());
            psmt.setString(3, point.getOrderId());
            psmt.setInt(4, point.getAmount());
            psmt.setString(5, point.getReason());
            psmt.setTimestamp(6, Timestamp.valueOf(point.getCreatedAt()));

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
