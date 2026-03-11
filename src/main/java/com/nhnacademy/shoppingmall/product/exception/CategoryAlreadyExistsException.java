package com.nhnacademy.shoppingmall.product.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String categoryId) {
        super(String.format("Category already exist:%s", categoryId));
    }
}

