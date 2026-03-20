package com.nhnacademy.shoppingmall.product.service;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;

import java.util.List;

public interface ProductService {
    String CONTEXT_PRODUCT_SERVICE_NAME = "CONTEXT_PRODUCT_SERVICE";

    void saveProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(String productId);

    Product getProduct(String productId);
    Page<Product> getProducts(int page, int pageSize);
    Page<Product> getProductsByCategory(String categoryId, int page, int pageSize);
    Page<Product> getProductsByName(String name, int page, int pageSize);
    List<Product> getProductsByIds(java.util.List<String> productIds);
}
