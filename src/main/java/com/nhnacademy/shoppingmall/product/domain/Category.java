package com.nhnacademy.shoppingmall.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Category {
    private String categoryId;
    private String categoryName;
    private int sortOrder;

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
}
