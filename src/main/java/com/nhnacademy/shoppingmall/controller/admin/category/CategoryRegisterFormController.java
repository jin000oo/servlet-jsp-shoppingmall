package com.nhnacademy.shoppingmall.controller.admin.category;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/admin/category/register.do")
public class CategoryRegisterFormController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return "admin/category_form";
    }
}
