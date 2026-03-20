package com.nhnacademy.shoppingmall.product.repository;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    int save(Product product);
    int update(Product product);

    Optional<Product> findById(String productId);
    Page<Product> findAll(int page, int pageSize);
    Page<Product> findByCategory(String categoryId, int page, int pageSize);
    Page<Product> findByName(String name, int page, int pageSize);
    List<Product> findByIds(java.util.List<String> productIds);

    int deleteById(String productId);
    boolean existsById(String productId);
}