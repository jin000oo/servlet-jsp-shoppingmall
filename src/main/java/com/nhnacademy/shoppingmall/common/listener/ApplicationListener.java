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

import com.nhnacademy.shoppingmall.address.repository.AddressRepository;
import com.nhnacademy.shoppingmall.address.repository.impl.AddressRepositoryImpl;
import com.nhnacademy.shoppingmall.address.service.AddressService;
import com.nhnacademy.shoppingmall.address.service.impl.AddressServiceImpl;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.repository.impl.CategoryRepositoryImpl;
import com.nhnacademy.shoppingmall.product.repository.impl.ProductRepositoryImpl;
import com.nhnacademy.shoppingmall.product.service.CategoryService;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.product.service.impl.CategoryServiceImpl;
import com.nhnacademy.shoppingmall.product.service.impl.ProductServiceImpl;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;

import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class ApplicationListener implements ServletContextListener {

    private UserService userService;
    private CategoryService categoryService;
    private ProductService productService;
    private AddressService addressService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        // Repository 객체 생성
        UserRepository userRepository = new UserRepositoryImpl();
        CategoryRepository categoryRepository = new CategoryRepositoryImpl();
        ProductRepository productRepository = new ProductRepositoryImpl();
        AddressRepository addressRepository = new AddressRepositoryImpl();

        // Service 객체 생성 및 의존성 주입 (DI)
        this.userService = new UserServiceImpl(userRepository);
        this.categoryService = new CategoryServiceImpl(categoryRepository);
        this.productService = new ProductServiceImpl(productRepository, categoryRepository);
        this.addressService = new AddressServiceImpl(addressRepository);

        // ServletContext에 Service 등록 (IoC Container 역할)
        context.setAttribute(UserService.CONTEXT_USER_SERVICE_NAME, this.userService);
        context.setAttribute(CategoryService.CONTEXT_CATEGORY_SERVICE_NAME, this.categoryService);
        context.setAttribute(ProductService.CONTEXT_PRODUCT_SERVICE_NAME, this.productService);
        context.setAttribute(AddressService.CONTEXT_ADDRESS_SERVICE_NAME, this.addressService);

        log.info("Application Context Initialized: Services injected into ServletContext.");

        try {
            DbConnectionThreadLocal.initialize();
            initUsers();

        } finally {
            DbConnectionThreadLocal.reset();
        }
    }

    //todo#12 application 시작시 테스트 계정인 admin,user 등록합니다. 만약 존재하면 등록하지 않습니다.
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
