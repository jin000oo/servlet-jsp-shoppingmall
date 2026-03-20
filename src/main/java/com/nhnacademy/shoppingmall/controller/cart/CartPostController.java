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

package com.nhnacademy.shoppingmall.controller.cart;

import com.nhnacademy.shoppingmall.cart.domain.Cart;
import com.nhnacademy.shoppingmall.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.shoppingmall.cart.service.CartService;
import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.POST, value = "/cart.do")
public class CartPostController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        CartService cartService = (CartService) req.getServletContext().getAttribute(CartService.CONTEXT_CART_SERVICE_NAME);
        String cartId = UUID.randomUUID().toString();
        String productId = req.getParameter("productId");
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");

        if (user != null) {
            // 회원 장바구니
            Cart cart = new Cart(cartId, user.getUserId(), productId, quantity);
            cartService.saveCart(cart);
        } else {
            // 비회원 장바구니
            Map<String, Cart> guestCart = (Map<String, Cart>) session.getAttribute("guestCart");

            if (guestCart == null) {
                guestCart = new HashMap<>();
            }

            if (guestCart.containsKey(productId)) {
                throw new CartAlreadyExistsException(productId);
            }

            guestCart.put(productId, new Cart(cartId, "guestCart", productId, quantity));
            session.setAttribute("guestCart", guestCart);
        }

        return "redirect:/cart.do";
    }

}
