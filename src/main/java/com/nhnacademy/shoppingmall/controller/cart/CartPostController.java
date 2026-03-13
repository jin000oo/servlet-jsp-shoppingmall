/// *
// * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// * + Copyright 2026. NHN Academy Corp. All rights reserved.
// * + * While every precaution has been taken in the preparation of this resource,  assumes no
// * + responsibility for errors or omissions, or for damages resulting from the use of the information
// * + contained herein
// * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
// * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
// * + prior written permission.
// * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// */
//
//package com.nhnacademy.shoppingmall.controller.cart;
//
//import com.nhnacademy.shoppingmall.cart.domain.Cart;
//import com.nhnacademy.shoppingmall.cart.service.CartService;
//import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
//import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//@RequestMapping(method = RequestMapping.Method.POST, value = "/cart.do")
//public class CartPostController implements BaseController {
//
//    private final CartService cartService;
//
//    public CartPostController(CartService cartService) {
//        this.cartService = cartService;
//    }
//
//    @Override
//    public String execute(HttpServletRequest req, HttpServletResponse resp) {
//        HttpSession session = req.getSession(false);
//
//        if (session != null && session.getAttribute("user") != null) {
//            String cartId = (String) req.getAttribute("cart_id");
//            String userId = (String) req.getAttribute("user_id");
//            String productId = (String) req.getAttribute("product_id");
//            int quantity = (int) req.getAttribute("quantity");
//
//            Cart cart = new Cart(cartId, userId, productId, quantity);
//            cartService.saveCart(cart);
//        }
//
//        return "shop/cart/cart";
//    }
//
//}
