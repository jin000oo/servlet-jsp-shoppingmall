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

package com.nhnacademy.shoppingmall.controller.point;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.service.PointService;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/mypage/point.do")
public class PointController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        PointService pointService = (PointService) req.getServletContext().getAttribute(PointService.CONTEXT_POINT_SERVICE_NAME);
        UserService userService = (UserService) req.getServletContext().getAttribute(UserService.CONTEXT_USER_SERVICE_NAME);

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            return "redirect:/login.do";
        }

        int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));

        Page<Point> pointPage = pointService.getPointList(user.getUserId(), page);
        req.setAttribute("pointList", pointPage.getContent());

        long totalCount = pointPage.getTotalCount();
        int totalPages = (int) Math.ceil((double) totalCount / Page.DEFAULT_PAGE_SIZE);

        if (totalPages == 0) {
            totalPages = 1;
        }

        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPoint", userService.getUser(user.getUserId()).getUserPoint());

        return "shop/point/point_history";
    }

}
