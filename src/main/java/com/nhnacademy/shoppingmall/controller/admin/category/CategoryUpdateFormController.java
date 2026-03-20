package com.nhnacademy.shoppingmall.controller.admin.category;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/admin/category/edit.do")
public class CategoryUpdateFormController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        CategoryService categoryService = (CategoryService) req.getServletContext().getAttribute(CategoryService.CONTEXT_CATEGORY_SERVICE_NAME);

        String categoryId = req.getParameter("id");
        if (categoryId == null || categoryId.trim().isEmpty()) {
            return "redirect:/admin/category.do";
        }

        Category category = categoryService.getCategory(categoryId);
        req.setAttribute("category", category);

        return "admin/category_form";
    }
}
