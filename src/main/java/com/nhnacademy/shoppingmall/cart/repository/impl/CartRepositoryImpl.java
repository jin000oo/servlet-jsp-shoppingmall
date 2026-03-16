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

package com.nhnacademy.shoppingmall.cart.repository.impl;

import com.nhnacademy.shoppingmall.cart.domain.Cart;
import com.nhnacademy.shoppingmall.cart.repository.CartRepository;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CartRepositoryImpl implements CartRepository {

    @Override
    public int save(Cart cart) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                INSERT INTO carts (cart_id, user_id, product_id, quantity) 
                VALUES (?, ?, ?, ?)
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, cart.getCartId());
            psmt.setString(2, cart.getUserId());
            psmt.setString(3, cart.getProductId());
            psmt.setInt(4, cart.getQuantity());

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Cart> findByUserIdAndProductId(String userId, String productId) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT cart_id, user_id, product_id, quantity 
                FROM carts 
                WHERE user_id = ? AND product_id = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            psmt.setString(2, productId);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    Cart cart = new Cart(
                            rs.getString("cart_id"),
                            rs.getString("user_id"),
                            rs.getString("product_id"),
                            rs.getInt("quantity")
                    );

                    return Optional.of(cart);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public List<Cart> findByUserId(String userId) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT cart_id, user_id, product_id, quantity 
                FROM carts 
                WHERE user_id = ?
                """;
        log.debug("sql:{}", sql);

        List<Cart> cartList = new ArrayList<>();

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);

            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    Cart cart = new Cart(
                            rs.getString("cart_id"),
                            rs.getString("user_id"),
                            rs.getString("product_id"),
                            rs.getInt("quantity")
                    );

                    cartList.add(cart);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return cartList;
    }

    @Override
    public int updateQuantity(String userId, String productId, int quantity) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                UPDATE carts SET quantity = ? 
                WHERE user_id = ? AND product_id = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setInt(1, quantity);
            psmt.setString(2, userId);
            psmt.setString(3, productId);

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByUserIdAndProductId(String userId, String productId) {
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                DELETE 
                FROM carts 
                WHERE user_id = ? AND product_id = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            psmt.setString(2, productId);

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
