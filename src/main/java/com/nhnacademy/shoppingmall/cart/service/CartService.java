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

package com.nhnacademy.shoppingmall.cart.service;

import com.nhnacademy.shoppingmall.cart.domain.Cart;
import java.util.List;

public interface CartService {

    // 장바구니에 상품 담기
    void saveCart(Cart cart);

    // 장바구니에 중복되는 상품을 담으려는지 확인
    Cart getCart(String userId, String productId);

    // 장바구니 목록 조회
    List<Cart> getCartList(String userId);

    // 장바구니 수량 변경
    void updateQuantity(String userId, String productId, int quantity);

    // 장바구니에서 특정 상품 삭제
    void deleteCart(String userId, String productId);

}
