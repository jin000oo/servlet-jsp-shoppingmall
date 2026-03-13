package com.nhnacademy.shoppingmall.product.service;

import com.nhnacademy.shoppingmall.product.domain.Category;

import java.util.List;

public interface CategoryService {
    String CONTEXT_CATEGORY_SERVICE_NAME = "CONTEXT_CATEGORY_SERVICE";

    void saveCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(String categoryId);

    Category getCategory(String categoryId);
    List<Category> getCategories();
    List<Category> getCategoriesByIds(List<String> categoryIds);
}
