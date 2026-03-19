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

import com.nhnacademy.shoppingmall.cart.domain.Cart;
import com.nhnacademy.shoppingmall.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.shoppingmall.cart.repository.CartRepository;
import com.nhnacademy.shoppingmall.cart.service.CartService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    public CartServiceImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public void saveCart(Cart cart) {
        validateCart(cart);

        if (cartRepository.findByUserIdAndProductId(cart.getUserId(), cart.getProductId()).isPresent()) {
            log.warn("[CartServiceImpl] cart already exists product: {}", cart.getProductId());
            throw new CartAlreadyExistsException(cart.getProductId());
        }

        cartRepository.save(cart);
    }

    @Override
    public Cart getCart(String userId, String productId) {
        validateId(userId);
        validateId(productId);

        return cartRepository.findByUserIdAndProductId(userId, productId)
                .orElse(null);
    }

    @Override
    public List<Cart> getCartList(String userId) {
        validateId(userId);

        return cartRepository.findByUserId(userId);
    }

    @Override
    public void updateQuantity(String userId, String productId, int quantity) {
        validateId(userId);
        validateId(productId);

        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be greater than 1");
        }

        cartRepository.updateQuantity(userId, productId, quantity);
    }

    @Override
    public void deleteCart(String userId, String productId) {
        validateId(userId);
        validateId(productId);

        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }

    private void validateId(String id) {
        if (id == null || id.isBlank()) {
            log.warn("[CartServiceImpl] id is null or blank");
            throw new IllegalArgumentException("[CartServiceImpl] id is null or blank");
        }
    }

    private void validateCart(Cart cart) {
        if (cart == null) {
            log.warn("[CartServiceImpl] cart is null");
            throw new IllegalArgumentException("[CartServiceImpl] cart is null");
        }
    }

}
