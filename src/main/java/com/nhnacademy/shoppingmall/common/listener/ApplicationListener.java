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

package com.nhnacademy.shoppingmall.common.listener;
import com.nhnacademy.shoppingmall.common.mvc.service.ServiceFactory;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class ApplicationListener implements ServletContextListener {

    private UserService userService;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        // ServiceFactory 초기화 및 서비스 등록
        Set<Class<?>> classes = (Set<Class<?>>) context.getAttribute("initializerClasses");
        ServiceFactory serviceFactory = new ServiceFactory();
        serviceFactory.initialize(classes, context);

        // ServletContext에서 Service 객체 가져오기 (이미 ServiceFactory에서 등록됨)
        this.userService = (UserService) context.getAttribute(UserService.CONTEXT_USER_SERVICE_NAME);

        log.info("Application Context Initialized: Services fetched from ServletContext.");

        try {
            DbConnectionThreadLocal.initialize();
            initUsers();

        } finally {
            DbConnectionThreadLocal.reset();
        }
    }

    // application 시작시 테스트 계정인 admin,user 등록합니다. 만약 존재하면 등록하지 않습니다.
    private void initUsers() {
        if (userService.getUser("admin") == null) {
            User admin = new User("admin", "관리자", "12345", "20031107",
                    User.Auth.ROLE_ADMIN, 1_000_000, LocalDateTime.now(), null);

            userService.saveUser(admin);
            log.debug("create admin test account - id: {} [{}]", admin.getUserId(), admin.getUserAuth());
        }

        if (userService.getUser("user") == null) {
            User user = new User("user", "사용자", "12345", "20010101",
                    User.Auth.ROLE_USER, 1_000_000, LocalDateTime.now(), null);

            userService.saveUser(user);
            log.debug("create user test account - id: {} [{}]", user.getUserId(), user.getUserAuth());
        }
    }
}
