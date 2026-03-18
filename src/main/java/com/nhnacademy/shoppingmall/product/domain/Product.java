package com.nhnacademy.shoppingmall.product.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product {
    public static final String NO_IMAGE_PATH = "/resources/no-image.png";
    public static final int MAX_CATEGORY_SIZE = 3;

    private final String productId;
    private String productName;
    private int price;
    private int stock;
    private List<String> categoryIds = new ArrayList<>();
    private String thumbnailImagePath;
    private List<String> detailImagePaths = new ArrayList<>();

    public Product(String productId, String productName, int price, int stock, List<String> categoryIds) {
        this(productId, productName, price, stock, NO_IMAGE_PATH, categoryIds, new ArrayList<>());
    }

    public Product(String productId, String productName, int price, int stock, String thumbnailImagePath, List<String> categoryIds, List<String> detailImagePaths) {
        validateCategories(categoryIds);

        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.categoryIds.addAll(categoryIds);

        this.thumbnailImagePath = thumbnailImagePath;
        if (detailImagePaths != null) {
            this.detailImagePaths.addAll(detailImagePaths);
        }
    }

    public List<String> getCategoryIds() {
         return Collections.unmodifiableList(this.categoryIds);
    }

    public void setCategoryIds(List<String> categoryIds) {
        validateCategories(categoryIds);
        this.categoryIds = categoryIds;
    }

    private void validateCategories(List<String> categoryIds) {
        // 카테고리 개수가 1개 이상 3개 이하인지 체크
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new IllegalArgumentException("A product must belong to at least 1 category.");
        }
        if (categoryIds.size() > MAX_CATEGORY_SIZE) {
            throw new IllegalArgumentException("A product can belong to a maximum of 3 categories.");
        }
    }
}