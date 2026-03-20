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

package com.nhnacademy.shoppingmall.user.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.annotation.Repository;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
        /* 회원의 아이디와 비밀번호를 이용해서 조회하는 코드 입니다.(로그인)
           해당 코드는 SQL Injection이 발생합니다. SQL Injection이 발생하지 않도록 수정하세요.
         */
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT user_id, user_name, user_password, user_birth, user_auth, user_point, created_at, latest_login_at 
                FROM users 
                WHERE user_id = ? AND user_password = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            psmt.setString(2, userPassword);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("user_id"),
                            rs.getString("user_name"),
                            rs.getString("user_password"),
                            rs.getString("user_birth"),
                            User.Auth.valueOf(rs.getString("user_auth")),
                            rs.getInt("user_point"),
                            rs.getObject("created_at", LocalDateTime.class),
                            rs.getObject("latest_login_at", LocalDateTime.class)
                    );

                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return Optional.empty();
    }

    @Override
    public Optional<User> findById(String userId) {
        // 회원조회
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT * 
                FROM users 
                WHERE user_id = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getString("user_id"),
                            rs.getString("user_name"),
                            rs.getString("user_password"),
                            rs.getString("user_birth"),
                            User.Auth.valueOf(rs.getString("user_auth")),
                            rs.getInt("user_point"),
                            rs.getObject("created_at", LocalDateTime.class),
                            rs.getObject("latest_login_at", LocalDateTime.class)
                    );

                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return Optional.empty();
    }

    @Override
    public int save(User user) {
        // 회원등록, executeUpdate()을 반환합니다.
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                INSERT INTO users (user_id, user_name, user_password, user_birth, user_auth, user_point, created_at) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, user.getUserId());
            psmt.setString(2, user.getUserName());
            psmt.setString(3, user.getUserPassword());
            psmt.setString(4, user.getUserBirth());
            psmt.setString(5, String.valueOf(user.getUserAuth()));
            psmt.setInt(6, user.getUserPoint());
            psmt.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByUserId(String userId) {
        // 회원삭제, executeUpdate()을 반환합니다.
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                DELETE 
                FROM users 
                WHERE user_id = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(User user) {
        // 회원수정, executeUpdate()을 반환합니다.
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                UPDATE users SET user_name = ?, user_password = ?, user_birth = ?, user_auth = ?, user_point = ? 
                WHERE user_id = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, user.getUserName());
            psmt.setString(2, user.getUserPassword());
            psmt.setString(3, user.getUserBirth());
            psmt.setString(4, String.valueOf(user.getUserAuth()));
            psmt.setInt(5, user.getUserPoint());
            psmt.setString(6, user.getUserId());

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateLatestLoginAtByUserId(String userId, LocalDateTime latestLoginAt) {
        // 마지막 로그인 시간 업데이트, executeUpdate()을 반환합니다.
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                UPDATE users SET latest_login_at = ? 
                WHERE user_id = ?
                """;
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setTimestamp(1, Timestamp.valueOf(latestLoginAt));
            psmt.setString(2, userId);

            return psmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByUserId(String userId) {
        // userId와 일치하는 회원의 count를 반환합니다.
        Connection connection = DbConnectionThreadLocal.getConnection();

        String sql = """
                SELECT COUNT(*) 
                FROM users 
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

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    @Override
    public Page<User> findAll(int page, int pageSize) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        int offset = (page - 1) * pageSize;

        // user_auth ASC -> ADMIN, USER 순서로 정렬
        String sql = """
                SELECT *
                FROM users
                ORDER BY user_auth ASC, created_at DESC
                LIMIT ? OFFSET ?
                """;
        log.debug("sql:{}", sql);

        java.util.List<User> userList = new java.util.ArrayList<>();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setInt(1, pageSize);
            psmt.setInt(2, offset);

            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    userList.add(new User(
                            rs.getString("user_id"),
                            rs.getString("user_name"),
                            rs.getString("user_password"),
                            rs.getString("user_birth"),
                            User.Auth.valueOf(rs.getString("user_auth")),
                            rs.getInt("user_point"),
                            rs.getObject("created_at", LocalDateTime.class),
                            rs.getObject("latest_login_at", LocalDateTime.class)
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Page<>(userList, countUsers());
    }

    private long countUsers() {
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "SELECT count(*) FROM users";
        log.debug("sql:{}", sql);

        try (PreparedStatement psmt = connection.prepareStatement(sql);
             ResultSet rs = psmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
