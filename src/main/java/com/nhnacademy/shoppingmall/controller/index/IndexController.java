package com.nhnacademy.shoppingmall.controller.index;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.service.CategoryService;
import com.nhnacademy.shoppingmall.product.service.ProductService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = {"/index.do"})
public class IndexController implements BaseController {

    private static final int DEFAULT_PAGE_SIZE = 9;
    private static final String RECENTLY_VIEWED_COOKIE_NAME = "recently_viewed";

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        ProductService productService = (ProductService) req.getServletContext().getAttribute(ProductService.CONTEXT_PRODUCT_SERVICE_NAME);
        CategoryService categoryService = (CategoryService) req.getServletContext().getAttribute(CategoryService.CONTEXT_CATEGORY_SERVICE_NAME);

        // 1. Pagination Parameters
        int page = 1;
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
                if (page < 1) {
                    page = 1;
                }
            } catch (NumberFormatException e) {
                // parse error, page = 1
            }
        }

        // 2. Filter Parameters
        String categoryId = req.getParameter("categoryId");
        String searchName = req.getParameter("searchName");

        Page<Product> productPage;

        // 3. Fetch Products based on priority: Search > Category > All
        if (searchName != null && !searchName.isBlank()) {
            productPage = productService.getProductsByName(searchName.trim(), page, DEFAULT_PAGE_SIZE);
        } else if (categoryId != null && !categoryId.isBlank()) {
            productPage = productService.getProductsByCategory(categoryId, page, DEFAULT_PAGE_SIZE);
        } else {
            productPage = productService.getProducts(page, DEFAULT_PAGE_SIZE);
        }

        // 4. Fetch Categories for Sidebar
        List<Category> categories = categoryService.getCategories();

        // 5. Recently Viewed Products (from Cookie)
        List<Product> recentlyViewedProducts = getRecentlyViewedProducts(req, productService);

        // 6. Set Attributes
        req.setAttribute("products", productPage.getContent());
        req.setAttribute("totalCount", productPage.getTotalCount());

        int totalPages = (int) Math.ceil((double) productPage.getTotalCount() / DEFAULT_PAGE_SIZE);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("currentPage", page);

        req.setAttribute("categories", categories);
        req.setAttribute("selectedCategoryId", categoryId);
        req.setAttribute("searchName", searchName);
        req.setAttribute("recentlyViewedProducts", recentlyViewedProducts);

        return "shop/main/index";
    }

    private List<Product> getRecentlyViewedProducts(HttpServletRequest req, ProductService productService) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return new ArrayList<>();

        List<String> ids = Arrays.stream(cookies)
                .filter(c -> c.getName().equals(RECENTLY_VIEWED_COOKIE_NAME))
                .findFirst()
                .map(c -> Arrays.asList(c.getValue().split("/")))
                .orElse(List.of());

        List<Product> products = productService.getProductsByIds(ids);

        // 한 번에 불러온 값들은 DB에서 순서 보장이 안되므로 직접 정렬함
        List<Product> sortedProducts = new LinkedList<>();
        for(String id : ids) {
            for(Product product: products) {
                if(product.getProductId().equals(id)) {
                    sortedProducts.add(product);
                }
            }
        }

        return sortedProducts;
    }
}
