package com.nhnacademy.shoppingmall.product.service.impl;

import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.exception.CategoryAlreadyExistsException;
import com.nhnacademy.shoppingmall.product.exception.CategoryNotFoundException;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
import com.nhnacademy.shoppingmall.product.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void saveCategory(Category category) {
        if (categoryRepository.existsById(category.getCategoryId())) {
            throw new CategoryAlreadyExistsException(category.getCategoryId());
        }

        int result = categoryRepository.save(category);
        if (result < 1) {
            throw new RuntimeException("Failed to register category: " + category.getCategoryId());
        }
    }

    @Override
    public void updateCategory(Category category) {
        if (!categoryRepository.existsById(category.getCategoryId())) {
            throw new CategoryNotFoundException(category.getCategoryId());
        }

        int result = categoryRepository.update(category);
        if (result < 1) {
            throw new RuntimeException("Failed to modify category: " + category.getCategoryId());
        }
    }

    @Override
    public void deleteCategory(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(categoryId);
        }

        int result = categoryRepository.deleteById(categoryId);
        if (result < 1) {
            throw new RuntimeException("Failed to delete category: " + categoryId);
        }
    }

    @Override
    public Category getCategory(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getCategoriesByIds(List<String> categoryIds) {
        return categoryRepository.findByIds(categoryIds);
    }
}
