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

package com.nhnacademy.shoppingmall.controller.auth;

import com.nhnacademy.shoppingmall.cart.domain.Cart;
import com.nhnacademy.shoppingmall.cart.service.CartService;
import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequestMapping(method = RequestMapping.Method.POST, value = "/loginAction.do")
public class LoginPostController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        UserService userService = (UserService) req.getServletContext().getAttribute(UserService.CONTEXT_USER_SERVICE_NAME);
        CartService cartService = (CartService) req.getServletContext().getAttribute(CartService.CONTEXT_CART_SERVICE_NAME);

        // 로그인 구현, session은 60분동안 유지됩니다.
        String userId = req.getParameter("user_id");
        String userPassword = req.getParameter("user_password");

        try {
            User user = userService.doLogin(userId, userPassword);

            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(3600);

            Map<String, Cart> guestCart = (Map<String, Cart>) session.getAttribute("guestCart");

            // 비회원일 때 장바구니에 상품을 담았다면 (비회원 장바구니가 비어있지 않을 때)
            if (guestCart != null && !guestCart.isEmpty()) {
                for (Cart cart : guestCart.values()) {
                    // DB에 해당 상품이 이미 존재하는지 확인
                    Cart existCart = cartService.getCart(user.getUserId(), cart.getProductId());

                    if (existCart != null) {
                        // 존재하면 기존 수량, 비회원 상태에서 담은 수량 합쳐서 업데이트
                        int quantity = existCart.getQuantity() + cart.getQuantity();
                        cartService.updateQuantity(user.getUserId(), cart.getProductId(), quantity);
                    } else {
                        // 없으면 기존 비회원 장바구니에서 user_id만 바꿔서 insert
                        Cart newCart =
                                new Cart(cart.getCartId(), user.getUserId(), cart.getProductId(), cart.getQuantity());
                        cartService.saveCart(newCart);
                    }
                }

                // DB로 다 옮긴 후 세션에 있는 비회원 장바구니 삭제
                session.removeAttribute("guestCart");
            }

            return "redirect:/index.do";

        } catch (Exception e) {
            log.error("[LoginPostController] error: {}", e.getMessage(), e);
            return "shop/login/login_form";
        }
    }

}
