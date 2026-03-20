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

package com.nhnacademy.shoppingmall.order.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.annotation.Repository;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.order.domain.Order;
import com.nhnacademy.shoppingmall.order.repository.OrderRepository;
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
public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public int save(Order order) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                INSERT INTO orders (order_id, user_id, total_amount, created_at) 
                VALUES (?, ?, ?, ?)
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, order.getOrderId());
            psmt.setString(2, order.getUserId());
            psmt.setInt(3, order.getTotalAmount());
            psmt.setTimestamp(4, Timestamp.valueOf(order.getCreatedAt()));

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findByUserId(String userId, int limit, int offset) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT order_id, user_id, total_amount, created_at 
                FROM orders 
                WHERE user_id = ? 
                ORDER BY created_at DESC LIMIT ? OFFSET ?
                """;
        log.debug("sql:{}", sql);

        List<Order> orderList = new ArrayList<>();

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            psmt.setInt(2, limit);
            psmt.setInt(3, offset);

            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getString("order_id"),
                            rs.getString("user_id"),
                            rs.getInt("total_amount"),
                            Objects.nonNull(rs.getTimestamp("created_at")) ?
                                    rs.getTimestamp("created_at").toLocalDateTime() : null
                    );

                    orderList.add(order);
                }
            }

            return orderList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByUserId(String userId) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT COUNT(*) 
                FROM orders 
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

    @Override
    public int updateUserId(String oldUserId, String newUserId) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                UPDATE orders SET user_id = ? 
                WHERE user_id = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, newUserId);
            psmt.setString(2, oldUserId);

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
