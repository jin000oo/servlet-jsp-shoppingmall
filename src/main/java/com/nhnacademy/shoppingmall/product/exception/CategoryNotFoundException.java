package com.nhnacademy.shoppingmall.product.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String categoryId) {
        super(String.format("Category not found:%s", categoryId));
    }
}
