package com.nhnacademy.shoppingmall.product.service;

import com.nhnacademy.shoppingmall.product.domain.Category;

import java.util.List;

public interface CategoryService {
    Category getCategory(String categoryId);
    List<Category> getCategories();
    void registerCategory(Category category);
    void modifyCategory(Category category);
    void deleteCategory(String categoryId);
}
