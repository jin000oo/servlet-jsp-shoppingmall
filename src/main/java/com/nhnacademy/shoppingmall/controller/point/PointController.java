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
import com.nhnacademy.shoppingmall.point.domain.Point;
import com.nhnacademy.shoppingmall.point.repository.impl.PointRepositoryImpl;
import com.nhnacademy.shoppingmall.point.service.PointService;
import com.nhnacademy.shoppingmall.point.service.impl.PointServiceImpl;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/point.do")
public class PointController implements BaseController {

    private final PointService pointService = new PointServiceImpl(new PointRepositoryImpl(), new UserRepositoryImpl());
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user == null) {
            return "redirect:/login.do";
        }

        int page = req.getParameter("page") == null ? 1 : Integer.parseInt(req.getParameter("page"));
        int limit = 10;
        int offset = (page - 1) * limit;

        List<Point> pointList = pointService.getPointList(user.getUserId(), limit, offset);
        req.setAttribute("pointList", pointList);

        int totalCount = pointService.getTotalCount(user.getUserId());
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        if (totalPages == 0) {
            totalPages = 1;
        }

        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);

        User recentUser = userService.getUser(user.getUserId());
        req.setAttribute("currentPoint", recentUser.getUserPoint());

        return "shop/point/point_history";
    }

}
