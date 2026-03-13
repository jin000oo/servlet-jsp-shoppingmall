package com.nhnacademy.shoppingmall.controller.admin.user;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping(method = RequestMapping.Method.GET, value = "/admin/user.do")
public class AdminUserController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        UserService userService = (UserService) req.getServletContext().getAttribute(UserService.CONTEXT_USER_SERVICE_NAME);

        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                // Ignore and use default
            }
        }

        Page<User> userPage = userService.getUsers(page, Page.DEFAULT_PAGE_SIZE);

        req.setAttribute("users", userPage.getContent());
        req.setAttribute("totalCount", userPage.getTotalCount());
        req.setAttribute("currentPage", page);
        req.setAttribute("activeTab", "user");

        int totalPages = (int) Math.ceil((double) userPage.getTotalCount() / Page.DEFAULT_PAGE_SIZE);
        req.setAttribute("totalPages", totalPages > 0 ? totalPages : 1);

        return "/admin/index";
    }
}