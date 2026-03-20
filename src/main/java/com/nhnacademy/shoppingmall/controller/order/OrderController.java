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

package com.nhnacademy.shoppingmall.controller.order;

import com.nhnacademy.shoppingmall.cart.domain.Cart;
import com.nhnacademy.shoppingmall.cart.service.CartService;
import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/order.do")
public class OrderController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        CartService cartService = (CartService) req.getServletContext().getAttribute(CartService.CONTEXT_CART_SERVICE_NAME);
        UserService userService = (UserService) req.getServletContext().getAttribute(UserService.CONTEXT_USER_SERVICE_NAME);
        ProductService productService = (ProductService) req.getServletContext().getAttribute(ProductService.CONTEXT_PRODUCT_SERVICE_NAME);

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            return "redirect:/login.do";
        }

        List<Cart> cartList = cartService.getCartList(user.getUserId());

        int totalAmount = 0;

        Map<String, Product> productMap = new HashMap<>();

        for (Cart cart : cartList) {
            Product product = productService.getProduct(cart.getProductId());
            totalAmount += product.getPrice() * cart.getQuantity();
            productMap.put(cart.getProductId(), product);
        }

        req.setAttribute("cartList", cartList);
        req.setAttribute("currentPoint", userService.getUser(user.getUserId()).getUserPoint());
        req.setAttribute("totalAmount", totalAmount);
        req.setAttribute("productMap", productMap);

        return "shop/order/order";
    }

}
