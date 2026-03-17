package com.nhnacademy.shoppingmall.controller.admin.product;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Transactional
@RequestMapping(method = RequestMapping.Method.POST, value = "/admin/product/register.do")
public class ProductRegisterPostController implements BaseController {

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

        String[] detailImagePathsArr = req.getParameterValues("detailImagePaths");
        List<String> detailImagePaths = detailImagePathsArr != null ? Arrays.asList(detailImagePathsArr) : List.of();

        if (thumbnailImagePath == null || thumbnailImagePath.trim().isEmpty()) {
            thumbnailImagePath = Product.NO_IMAGE_PATH;
        }

        Product product = new Product(productId, productName, price, stock, thumbnailImagePath, categoryIds, detailImagePaths);
        productService.saveProduct(product);

        return "redirect:/admin/product.do";
    }
}