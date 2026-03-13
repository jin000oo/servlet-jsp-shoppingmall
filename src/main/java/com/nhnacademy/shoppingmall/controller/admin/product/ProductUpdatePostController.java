package com.nhnacademy.shoppingmall.controller.admin.product;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RequestMapping(method = RequestMapping.Method.POST, value = "/admin/product/edit.do")
public class ProductUpdatePostController implements BaseController {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        ProductService productService = (ProductService) req.getServletContext().getAttribute(ProductService.CONTEXT_PRODUCT_SERVICE_NAME);

        String productId = req.getParameter("productId");
        String productName = req.getParameter("productName");
        int price = Integer.parseInt(req.getParameter("price"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        String thumbnailImagePath = req.getParameter("thumbnailImagePath");
        
        String[] categoryIdsArr = req.getParameterValues("categoryIds");
        List<String> categoryIds = categoryIdsArr != null ? Arrays.asList(categoryIdsArr) : List.of();

        if (thumbnailImagePath == null || thumbnailImagePath.trim().isEmpty()) {
            thumbnailImagePath = Product.NO_IMAGE_PATH;
        }

        Product product = new Product(productId, productName, price, stock, thumbnailImagePath, categoryIds);
        productService.updateProduct(product);

        return "redirect:/admin/product.do";
    }
}