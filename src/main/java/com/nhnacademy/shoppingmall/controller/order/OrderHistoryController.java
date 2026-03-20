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

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.order.domain.Order;
import com.nhnacademy.shoppingmall.order.domain.OrderDetail;
import com.nhnacademy.shoppingmall.order.service.OrderService;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/mypage/history.do")
public class OrderHistoryController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        OrderService orderService = (OrderService) req.getServletContext().getAttribute(OrderService.CONTEXT_ORDER_SERVICE_NAME);
        ProductService productService = (ProductService) req.getServletContext().getAttribute(ProductService.CONTEXT_PRODUCT_SERVICE_NAME);

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            return "redirect:/login.do";
        }

        int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));

        Page<Order> orderPage = orderService.getOrderList(user.getUserId(), page);
        req.setAttribute("orderList", orderPage.getContent());

        long totalCount = orderPage.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / Page.DEFAULT_PAGE_SIZE);

        if (totalPages == 0) {
            totalPages = 1;
        }

        Map<String, List<OrderDetail>> orderDetailMap = new HashMap<>();
        Map<String, Product> productMap = new HashMap<>();

        for (Order order : orderPage.getContent()) {
            List<OrderDetail> details = orderService.getOrderDetails(order.getOrderId());
            orderDetailMap.put(order.getOrderId(), details);

            for (OrderDetail detail : details) {
                if (!productMap.containsKey(detail.getProductId())) {
                    Product product = productService.getProduct(detail.getProductId());
                    productMap.put(detail.getProductId(), product);
                }
            }
        }

        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("orderDetailMap", orderDetailMap);
        req.setAttribute("productMap", productMap);

        return "shop/order/order_history";
    }

}
