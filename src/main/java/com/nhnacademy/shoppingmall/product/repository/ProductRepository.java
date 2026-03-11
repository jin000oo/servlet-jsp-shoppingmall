package com.nhnacademy.shoppingmall.product.repository;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;

import java.util.Optional;

public interface ProductRepository {

    int save(Product product);
    int update(Product product);

    Optional<Product> findById(String productId);
    Page<Product> findAll(int page, int pageSize);
    Page<Product> findByCategory(String categoryId, int page, int pageSize);
    // Page<Product> findByProductName(String keyword, int page, int pageSize);

    int saveProductCategory(String productId, String categoryId);
    int deleteCategoriesByProductId(String productId);

    int deleteById(String productId);
    int countById(String productId);
}
