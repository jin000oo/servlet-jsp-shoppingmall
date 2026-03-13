package com.nhnacademy.shoppingmall.controller.admin.category;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequestMapping(method = RequestMapping.Method.POST, value = "/admin/category/register.do")
public class CategoryRegisterPostController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        CategoryService categoryService = (CategoryService) req.getServletContext().getAttribute(CategoryService.CONTEXT_CATEGORY_SERVICE_NAME);

        String categoryId = req.getParameter("categoryId");
        String categoryName = req.getParameter("categoryName");
        int sortOrder = Integer.parseInt(req.getParameter("sortOrder") != null ? req.getParameter("sortOrder") : "0");

        Category category = new Category(categoryId, categoryName, sortOrder);
        categoryService.saveCategory(category);

        return "redirect:/admin/category.do";
    }
}
