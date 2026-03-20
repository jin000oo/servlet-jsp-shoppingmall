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
import com.nhnacademy.shoppingmall.order.domain.OrderDetail;
import com.nhnacademy.shoppingmall.order.repository.OrderDetailRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class OrderDetailRepositoryImpl implements OrderDetailRepository {

    @Override
    public int save(OrderDetail orderDetail) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                INSERT INTO order_details (order_detail_id, order_id, product_id, quantity) 
                VALUES (?, ?, ?, ?)
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, orderDetail.getOrderDetailId());
            psmt.setString(2, orderDetail.getOrderId());
            psmt.setString(3, orderDetail.getProductId());
            psmt.setInt(4, orderDetail.getQuantity());

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderDetail> findByOrderId(String orderId) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT order_detail_id, order_id, product_id, quantity 
                FROM order_details 
                WHERE order_id = ?
                """;
        log.debug("sql:{}", sql);

        List<OrderDetail> orderDetailList = new ArrayList<>();

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, orderId);

            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    OrderDetail orderDetail = new OrderDetail(
                            rs.getString("order_detail_id"),
                            rs.getString("order_id"),
                            rs.getString("product_id"),
                            rs.getInt("quantity")
                    );

                    orderDetailList.add(orderDetail);
                }
            }

            return orderDetailList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
