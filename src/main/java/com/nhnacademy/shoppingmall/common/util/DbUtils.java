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

package com.nhnacademy.shoppingmall.common.util;


import java.sql.SQLException;
import java.time.Duration;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class DbUtils {

    private DbUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final DataSource DATASOURCE;

    static {
        BasicDataSource basicDataSource = new BasicDataSource();

        try {
            basicDataSource.setDriver(new com.mysql.cj.jdbc.Driver());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // {ip},{database},{username},{password} 설정
        basicDataSource.setUrl("jdbc:mysql://s4.java21.net:13306/nhn_academy_24");
        basicDataSource.setUsername("nhn_academy_24");
        basicDataSource.setPassword("WsWFMQn[h*Ukt2Fb");

        // initialSize, maxTotal, maxIdle, minIdle 은 모두 5로 설정합니다.
        basicDataSource.setInitialSize(5);
        basicDataSource.setMaxTotal(5);
        basicDataSource.setMaxIdle(5);
        basicDataSource.setMinIdle(5);

        // Validation Query를 설정하세요
        basicDataSource.setValidationQuery("select 1");

        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setMaxWait(Duration.ofSeconds(2));

        // 적절히 변경하세요
        DATASOURCE = basicDataSource;
    }

    public static DataSource getDataSource() {
        return DATASOURCE;
    }

}
