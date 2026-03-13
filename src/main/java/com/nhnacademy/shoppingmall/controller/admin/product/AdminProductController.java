package com.nhnacademy.shoppingmall.controller.admin.product;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.service.CategoryService;
import com.nhnacademy.shoppingmall.product.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(method = RequestMapping.Method.GET, value = "/admin/product.do")
public class AdminProductController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        ProductService productService = (ProductService) req.getServletContext().getAttribute(ProductService.CONTEXT_PRODUCT_SERVICE_NAME);
        CategoryService categoryService = (CategoryService) req.getServletContext().getAttribute(CategoryService.CONTEXT_CATEGORY_SERVICE_NAME);

        int page = 1;
        int pageSize = 10;

        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                // 페이지 파라미터가 지정되어 있지 않으므로 자동으로 page = 1로 설정
            }
        }

        Page<Product> productPage = productService.getProducts(page, pageSize);
        
        List<String> categoryIdsOnPage = productPage.getContent().stream()
                .map(Product::getCategoryIds)
                .flatMap(Collection::stream)
                .distinct()
                .toList();

        Map<String, String> categoryMap = categoryService.getCategoriesByIds(categoryIdsOnPage).stream()
                .collect(Collectors.toMap(Category::getCategoryId, Category::getCategoryName));
        
        req.setAttribute("products", productPage.getContent());
        req.setAttribute("categoryMap", categoryMap);
        req.setAttribute("totalCount", productPage.getTotalCount());
        req.setAttribute("currentPage", page);
        req.setAttribute("activeTab", "product");
        
        int totalPages = (int) Math.ceil((double) productPage.getTotalCount() / pageSize);
        req.setAttribute("totalPages", totalPages > 0 ? totalPages : 1);

        return "/admin/index";
    }
}
