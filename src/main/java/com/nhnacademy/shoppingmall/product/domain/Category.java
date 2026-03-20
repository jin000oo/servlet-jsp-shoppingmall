package com.nhnacademy.shoppingmall.product.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Category {
    private final String categoryId;
    private String categoryName;
    private int sortOrder;
}
