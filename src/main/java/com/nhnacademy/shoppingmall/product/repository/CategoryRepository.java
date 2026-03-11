package com.nhnacademy.shoppingmall.product.repository;

import com.nhnacademy.shoppingmall.product.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    int save(Category category);
    int update(Category category);

    Optional<Category> findById(String categoryId);
    List<Category> findAll();

    int deleteById(String categoryId);
    int countById(String categoryId);
}