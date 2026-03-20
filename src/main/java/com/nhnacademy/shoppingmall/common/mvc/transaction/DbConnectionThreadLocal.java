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

package com.nhnacademy.shoppingmall.common.mvc.transaction;

import com.nhnacademy.shoppingmall.common.util.DbUtils;
import java.sql.Connection;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbConnectionThreadLocal {

    private static final ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> sqlErrorThreadLocal = ThreadLocal.withInitial(() -> false);

    public static void initialize() {
        try {
            // connection pool에서 connectionThreadLocal에 connection을 할당합니다.
            Connection connection = DbUtils.getDataSource().getConnection();
            connectionThreadLocal.set(connection);

            // connection의 Isolation level을 READ_COMMITED를 설정 합니다.
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            // auto commit 을 false로 설정합니다.
            connection.setAutoCommit(false);

        } catch (SQLException e) {
            log.error("initialize - sql exception: {}", e.getMessage());
            connectionThreadLocal.remove();
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connectionThreadLocal.get();
    }

    public static void setSqlError(boolean sqlError) {
        sqlErrorThreadLocal.set(sqlError);
    }

    public static boolean getSqlError() {
        return sqlErrorThreadLocal.get();
    }

    public static void reset() {
        // 사용이 완료된 connection은 close를 호출하여 connection pool에 반환합니다.
        try (Connection connection = getConnection()) {
            // getSqlError() 에러가 존재하면 rollback 합니다.
            if (getSqlError()) {
                log.debug("reset - sql error: rollback");
                connection.rollback();
            }

            // getSqlError() 에러가 존재하지 않다면 commit 합니다.
            else {
                connection.commit();
            }

        } catch (SQLException e) {
            log.error("reset - sql exception: {}", e.getMessage());
            throw new RuntimeException(e);

        } finally {
            // 현제 사용하고 있는 connection을 재 사용할 수 없도록 connectionThreadLocal을 초기화 합니다.
            connectionThreadLocal.remove();
            sqlErrorThreadLocal.remove();
        }
    }

}
