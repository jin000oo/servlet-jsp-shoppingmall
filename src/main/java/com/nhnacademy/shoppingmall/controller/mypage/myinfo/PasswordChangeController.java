package com.nhnacademy.shoppingmall.controller.mypage.myinfo;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping(method = RequestMapping.Method.GET, value = "/mypage/password.do")
public class PasswordChangeController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return "shop/mypage/password_form";
    }
}
