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
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.repository.impl.CategoryRepositoryImpl;
import com.nhnacademy.shoppingmall.product.repository.impl.ProductRepositoryImpl;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class CartRepositoryImplTest {

    CartRepository cartRepository = new CartRepositoryImpl();
    UserRepository userRepository = new UserRepositoryImpl();
    ProductRepository productRepository = new ProductRepositoryImpl();
    CategoryRepository categoryRepository = new CategoryRepositoryImpl();

    Cart testCart;
    User testUser;
    Product testProduct;
    Category testCategory;

    @BeforeEach
    void setUp() throws SQLException {
        DbConnectionThreadLocal.initialize();

        testUser = new User(
                "test-user",
                "nhn아카데미",
                "nhnacademy-test-password",
                "19900505",
                User.Auth.ROLE_USER,
                100_0000,
                LocalDateTime.now(),
                null
        );

        testCategory = new Category(
                "test-category",
                "test-category-name",
                1
        );

        List<String> categories = new ArrayList<>();
        categories.add("test-category");

        testProduct = new Product(
                "test-product",
                "test-product-name",
                10_000,
                100,
                categories
        );

        testCart = new Cart(
                "test-cart1",
                "test-user",
                "test-product",
                100
        );

        userRepository.save(testUser);
        categoryRepository.save(testCategory);
        productRepository.save(testProduct);
    }

    @AfterEach
    void tearDown() throws SQLException {
        DbConnectionThreadLocal.setSqlError(true);
        DbConnectionThreadLocal.reset();
    }

    @Test
    @Order(1)
    @DisplayName("장바구니 등록 및 조회")
    void saveAndFindByUserIdAndProductId() {
        int result = cartRepository.save(testCart);
        Optional<Cart> savedCart =
                cartRepository.findByUserIdAndProductId(testCart.getUserId(), testCart.getProductId());

        Assertions.assertEquals(1, result);
        Assertions.assertTrue(savedCart.isPresent());
        Assertions.assertEquals(100, savedCart.get().getQuantity());
    }

    @Test
    @Order(2)
    @DisplayName("장바구니 목록 조회")
    void findByUserId() {
        cartRepository.save(testCart);

        List<String> categories = new ArrayList<>();
        categories.add("test-category");

        Product testProduct2 = new Product(
                "test-product2",
                "test-product-name2",
                1_000,
                1_000,
                categories
        );
        productRepository.save(testProduct2);

        Cart testCart2 = new Cart(
                "test-cart2",
                "test-user",
                "test-product2",
                50
        );
        cartRepository.save(testCart2);

        List<Cart> cartList = cartRepository.findByUserId("test-user");

        Assertions.assertEquals(2, cartList.size());
    }

    @Test
    @Order(3)
    @DisplayName("장바구니 수량 업데이트")
    void updateQuantityByCartId() {
        cartRepository.save(testCart);

        int result = cartRepository.updateQuantity(testCart.getUserId(), testCart.getProductId(), 10);
        Optional<Cart> updatedCart =
                cartRepository.findByUserIdAndProductId(testCart.getUserId(), testCart.getProductId());

        Assertions.assertEquals(1, result);
        Assertions.assertTrue(updatedCart.isPresent());
        Assertions.assertEquals(10, updatedCart.get().getQuantity());
    }

    @Test
    @Order(4)
    @DisplayName("주문 완료 후 장바구니 삭제")
    void deleteByUserIdAndProductId() {
        cartRepository.save(testCart);

        int result = cartRepository.deleteByUserIdAndProductId(testCart.getUserId(), testCart.getProductId());
        Optional<Cart> deletedCart =
                cartRepository.findByUserIdAndProductId(testCart.getUserId(), testCart.getProductId());

        Assertions.assertEquals(1, result);
        Assertions.assertFalse(deletedCart.isPresent());
    }

}
