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

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.domain.Product;
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
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class ApplicationListener implements ServletContextListener {

    private UserService userService;
    private CategoryService categoryService;
    private ProductService productService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        // Repository 객체 생성
        UserRepository userRepository = new UserRepositoryImpl();
        CategoryRepository categoryRepository = new CategoryRepositoryImpl();
        ProductRepository productRepository = new ProductRepositoryImpl();

        // Service 객체 생성 및 의존성 주입 (DI)
        this.userService = new UserServiceImpl(userRepository);
        this.categoryService = new CategoryServiceImpl(categoryRepository);
        this.productService = new ProductServiceImpl(productRepository, categoryRepository);

        // ServletContext에 Service 등록 (IoC Container 역할)
        // context.setAttribute("userService", this.userService); // todo: userService를 ServletContext에 등록하여 사용하도록 수정
        context.setAttribute(CategoryService.CONTEXT_CATEGORY_SERVICE_NAME, this.categoryService);
        context.setAttribute(ProductService.CONTEXT_PRODUCT_SERVICE_NAME, this.productService);

        log.info("Application Context Initialized: Services injected into ServletContext.");

        try {
            DbConnectionThreadLocal.initialize();

            initUsers();
            // TODO: 테스트를 위한 상품 및 카테고리 초기화. 추후 삭제합니다.
            initCategories();
            initProducts();

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

    private void initCategories() {
        List<Category> initCategoryList = Arrays.asList(
                new Category("C001", "남성 의류", 1),
                new Category("C002", "여성 의류", 2),
                new Category("C003", "컴퓨터", 3),
                new Category("C004", "도서", 4)
        );

        for (Category category : initCategoryList) {
            try {
                categoryService.deleteCategory(category.getCategoryId());
                categoryService.saveCategory(category);
            } catch (RuntimeException e) {
                // 이미 존재 할 경우, 초기화합니다.
                log.debug("[InitCategory] Failed to Initialize category ID:{}", category.getCategoryId());
            }
        }
        log.debug("create test categories");
    }

    private void initProducts() {
        List<Product> initProductList = Arrays.asList(
                new Product("P001", "남성 반팔티", 25000, 10, List.of("C001")),
                new Product("P002", "남성 청바지", 30000, 10, List.of("C001")),
                new Product("P003", "여성 원피스", 45000, 10, List.of("C002")),
                new Product("P004", "여성 가디건", 35000, 10, List.of("C002")),
                new Product("P005", "남녀공용 후드티", 40000, 15, List.of("C001", "C002")),
                new Product("P006", "게이밍 마우스", 85000, 20, List.of("C003")),
                new Product("P007", "기계식 키보드", 150000, 5, List.of("C003")),
                new Product("P008", "개발자 노트북", 1000000, 3, List.of("C003")),
                new Product("P009", "자바 프로그래밍", 30000, 30, List.of("C004")),
                new Product("P010", "스프링 부트 기초", 35000, 20, List.of("C004"))
        );

        for (Product product : initProductList) {
            try {
                productService.deleteProduct(product.getProductId());
                productService.saveProduct(product);
            } catch (RuntimeException e) {
                // 실패할 경우 스킵
                log.debug("[InitProduct] Failed to Initialize product ID:{}", product.getProductId());
            }
        }
        log.debug("create test products");
    }
}
