package com.nhnacademy.shoppingmall.controller.admin.category;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/admin/category.do")
public class AdminCategoryController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        CategoryService categoryService = (CategoryService) req.getServletContext().getAttribute(CategoryService.CONTEXT_CATEGORY_SERVICE_NAME);

        List<Category> categories = categoryService.getCategories();
        
        req.setAttribute("categories", categories);
        req.setAttribute("activeTab", "category");

        return "/admin/index";
    }
}
