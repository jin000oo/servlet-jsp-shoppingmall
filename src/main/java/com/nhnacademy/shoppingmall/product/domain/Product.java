package com.nhnacademy.shoppingmall.product.domain;

import lombok.Getter;

@Getter
public class Product {
    public static final String NO_IMAGE_PATH = "/resources/no-image.png";

    private String productId;
    private String productName;
    private int price;
    private int stock;
    private String thumbnailImagePath;

    public Product(String productId, String productName, int price, int stock) {
        this(productId, productName, price, stock, NO_IMAGE_PATH);
    }

    public Product(String productId, String productName, int price, int stock, String thumbnailImagePath) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.thumbnailImagePath = thumbnailImagePath;
    }
}
