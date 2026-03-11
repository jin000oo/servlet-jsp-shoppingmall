package com.nhnacademy.shoppingmall.product.service;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;

import java.util.List;

public interface ProductService {
    void registerProduct(Product product, List<String> categoryIds);
    void modifyProduct(Product product, List<String> categoryIds);
    void deleteProduct(String productId);

    Product getProduct(String productId);
    Page<Product> getProducts(int page, int pageSize);
    Page<Product> getProductsByCategory(String categoryId, int page, int pageSize);
}
