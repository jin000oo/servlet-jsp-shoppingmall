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

    void saveCart(Cart cart);

    Cart getCart(String userId, String productId);

    List<Cart> getCartList(String userId);

    void updateQuantity(String userId, String productId, int quantity);

    void deleteCart(String userId, String productId);

}
