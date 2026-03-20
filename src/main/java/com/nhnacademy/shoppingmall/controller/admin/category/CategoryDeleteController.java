package com.nhnacademy.shoppingmall.controller.admin.category;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Transactional
@RequestMapping(method = RequestMapping.Method.POST, value = "/admin/category/delete.do")
public class CategoryDeleteController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        CategoryService categoryService = (CategoryService) req.getServletContext().getAttribute(CategoryService.CONTEXT_CATEGORY_SERVICE_NAME);

        String categoryId = req.getParameter("categoryId");
        
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            categoryService.deleteCategory(categoryId);
        }

        return "redirect:/admin/category.do";
    }
}
