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

package com.nhnacademy.shoppingmall.cart.repository;

import com.nhnacademy.shoppingmall.cart.domain.Cart;
import java.util.List;
import java.util.Optional;

public interface CartRepository {

    // 새 상품 담기
    int save(Cart cart);

    // 중복으로 담으려는지 확인
    Optional<Cart> findByUserIdAndProductId(String userId, String productId);

    // 장바구니 목록 조회
    List<Cart> findByUserId(String userId);

    // 장바구니 수량 변경
    int updateQuantityByCartId(String cartId, int quantity);

    // 주문 완료 후 장바구니에서 비우기
    int deleteByUserIdAndProductId(String userId, String productId);

}
