package com.nhnacademy.shoppingmall.product.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Product {
    public static final String NO_IMAGE_PATH = "/resources/no-image.png";

    private String productId;
    private String productName;
    private int price;
    private int stock;
    private List<String> categoryIds = new ArrayList<>();

    private String thumbnailImagePath;

    private List<String> detailImagePaths = new ArrayList<>();

    public Product(String productId, String productName, int price, int stock, List<String> categoryIds) {
        this(productId, productName, price, stock, NO_IMAGE_PATH, categoryIds);
    }

    public Product(String productId, String productName, int price, int stock, String thumbnailImagePath, List<String> categoryIds) {
        validateCategories(categoryIds);

        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.categoryIds.addAll(categoryIds);

        this.thumbnailImagePath = thumbnailImagePath;
    }

    private void validateCategories(List<String> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new IllegalArgumentException("A product must belong to at least 1 category.");
        }
        if (categoryIds.size() > 3) {
            throw new IllegalArgumentException("A product can belong to a maximum of 3 categories.");
        }
    }

    public List<String> getCategoryIds() {
         return Collections.unmodifiableList(this.categoryIds);
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setThumbnailImagePath(String thumbnailImagePath) {
        this.thumbnailImagePath = thumbnailImagePath;
    }

    public void setCategoryIds(List<String> categoryIds) {
        validateCategories(categoryIds);
        this.categoryIds = categoryIds;
    }

    public void setDetailImagePaths(List<String> detailImagePaths) {
        this.detailImagePaths = detailImagePaths;
    }
}