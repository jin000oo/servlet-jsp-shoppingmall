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

package com.nhnacademy.shoppingmall.cart.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.shoppingmall.cart.domain.Cart;
import com.nhnacademy.shoppingmall.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.shoppingmall.cart.repository.CartRepository;
import com.nhnacademy.shoppingmall.cart.service.CartService;
import com.nhnacademy.shoppingmall.order.exception.InsufficientQuantityException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    CartRepository cartRepository = Mockito.mock(CartRepository.class);
    CartService cartService = new CartServiceImpl(cartRepository);

    Cart testCart = new Cart(
            "test-cart1",
            "test-user",
            "test-product",
            100
    );

    @Test
    @DisplayName("장바구니 담기")
    void saveCart() {
        when(cartRepository.findByUserIdAndProductId(anyString(), anyString())).thenReturn(Optional.empty());

        cartService.saveCart(testCart);

        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니 담기 실패 - 중복된 상품")
    void saveCart_fail() {
        when(cartRepository.findByUserIdAndProductId(anyString(), anyString())).thenReturn(Optional.of(testCart));

        Assertions.assertThrows(CartAlreadyExistsException.class, () ->
                cartService.saveCart(testCart));

        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니 목록 조회")
    void getCartList() {
        when(cartRepository.findByUserId(testCart.getUserId())).thenReturn(List.of(testCart));

        List<Cart> cartList = cartService.getCartList(testCart.getUserId());

        Assertions.assertEquals(1, cartList.size());
        verify(cartRepository, times(1)).findByUserId(testCart.getUserId());
    }

    @Test
    @DisplayName("장바구니 수량 변경")
    void updateQuantity() {
        cartService.updateQuantity(testCart.getUserId(), testCart.getProductId(), 10);

        verify(cartRepository, times(1))
                .updateQuantity(testCart.getUserId(), testCart.getProductId(), 10);
    }

    @Test
    @DisplayName("장바구니 수량 변경 실패 - 수량이 1보다 작을 때")
    void updateQuantity_fail() {
        Assertions.assertThrows(InsufficientQuantityException.class, () ->
                cartService.updateQuantity(testCart.getUserId(), testCart.getProductId(), 0));

        verify(cartRepository, never()).updateQuantity(anyString(), anyString(), anyInt());
    }

    @Test
    @DisplayName("주문 완료 후 장바구니 목록 삭제")
    void deleteCart() {
        cartService.deleteCart(testCart.getUserId(), testCart.getProductId());

        verify(cartRepository, times(1))
                .deleteByUserIdAndProductId(testCart.getUserId(), testCart.getProductId());
    }

    @Test
    @DisplayName("아이디 검증 실패 - null 또는 공백")
    void validateId_fail() {
        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        cartService.getCart(null, null)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        cartService.getCart("", "")),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        cartService.getCartList(null)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () ->
                        cartService.getCartList(""))
        );
    }

    @Test
    @DisplayName("객체 검증 실패 - null")
    void validateCart_fail() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                cartService.saveCart(null));
    }

}
