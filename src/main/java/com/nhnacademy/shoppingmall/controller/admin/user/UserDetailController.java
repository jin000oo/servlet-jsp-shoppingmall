package com.nhnacademy.shoppingmall.controller.admin.user;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping(method = RequestMapping.Method.GET, value = "/admin/user/detail.do")
public class UserDetailController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        UserService userService = (UserService) req.getServletContext().getAttribute(UserService.CONTEXT_USER_SERVICE_NAME);

        String userId = req.getParameter("id");
        if (userId == null || userId.trim().isEmpty()) {
            return "redirect:/admin/user.do";
        }

        User user = userService.getUser(userId);
        if (user == null) {
            log.error("Cannot find user Id: {}", userId);
            return "redirect:/admin/user.do";
        }

        req.setAttribute("userDetail", user);

        return "admin/user_detail";
    }
}