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

import com.nhnacademy.shoppingmall.common.mvc.annotation.Repository;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.repository.PointRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
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

    @Override
    public List<Point> findByUserId(String userId, int limit, int offset) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT point_id, user_id, order_id, amount, reason, created_at 
                FROM points 
                WHERE user_id = ? 
                ORDER BY created_at DESC LIMIT ? OFFSET ?
                """;
        log.debug("sql:{}", sql);

        List<Point> pointList = new ArrayList<>();

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            psmt.setInt(2, limit);
            psmt.setInt(3, offset);

            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    Point point = new Point(
                            rs.getString("point_id"),
                            rs.getString("user_id"),
                            rs.getString("order_id"),
                            rs.getInt("amount"),
                            rs.getString("reason"),
                            Objects.nonNull(rs.getTimestamp("created_at")) ?
                                    rs.getTimestamp("created_at").toLocalDateTime() : null
                    );

                    pointList.add(point);
                }
            }

            return pointList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByUserId(String userId) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT COUNT(*) 
                FROM points 
                WHERE user_id = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            return 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
