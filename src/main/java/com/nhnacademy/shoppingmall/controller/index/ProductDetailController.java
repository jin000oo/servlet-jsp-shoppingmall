package com.nhnacademy.shoppingmall.controller.index;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.service.CategoryService;
import com.nhnacademy.shoppingmall.product.service.ProductService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequestMapping(method = RequestMapping.Method.GET, value = "/product.do")
public class ProductDetailController implements BaseController {

    private static final String RECENTLY_VIEWED_COOKIE_NAME = "recently_viewed";
    private static final int MAX_RECENTLY_VIEWED = 5;
    private static final int MAX_AGE = 60 * 60 * 24 * 3; // 3일

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        ProductService productService = (ProductService) req.getServletContext().getAttribute(ProductService.CONTEXT_PRODUCT_SERVICE_NAME);
        CategoryService categoryService = (CategoryService) req.getServletContext().getAttribute(CategoryService.CONTEXT_CATEGORY_SERVICE_NAME);

        String productId = req.getParameter("id");
        if (productId == null || productId.isEmpty()) {
            return "redirect:/index.do";
        }

        Product product = productService.getProduct(productId);
        Map<String, String> categoryMap = categoryService.getCategoriesByIds(product.getCategoryIds()).stream()
                        .collect(Collectors.toMap(Category::getCategoryId, Category::getCategoryName));

        req.setAttribute("product", product);
        req.setAttribute("categoryMap", categoryMap);
        // Update Recently Viewed Cookie
        updateRecentlyViewedCookie(req, resp, productId);

        return "shop/product/detail";
    }

    private void updateRecentlyViewedCookie(HttpServletRequest req, HttpServletResponse resp, String productId) {
        Cookie[] cookies = req.getCookies();
        String recentlyViewedValue = "";

        // 저장된 쿠키 불러오기
        if (cookies != null) {
            recentlyViewedValue = Arrays.stream(cookies)
                    .filter(c -> c.getName().equals(RECENTLY_VIEWED_COOKIE_NAME))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse("");
        }

        // 불러온 쿠키 파싱하여 ID 리스트로 변환
        List<String> ids = new ArrayList<>();
        if (!recentlyViewedValue.isEmpty()) {
            ids.addAll(Arrays.asList(recentlyViewedValue.split("/")));
        }

        // 이미 존재하면 삭제 후 등록
        ids.remove(productId);
        ids.addFirst(productId);

        // 최대 개수만큼 리스트 자르기
        if (ids.size() > MAX_RECENTLY_VIEWED) {
            ids = ids.subList(0, MAX_RECENTLY_VIEWED);
        }

        String newValue = String.join("/", ids);
        Cookie cookie = new Cookie(RECENTLY_VIEWED_COOKIE_NAME, newValue);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE);
        resp.addCookie(cookie);
    }
}
