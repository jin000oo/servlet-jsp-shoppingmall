package com.nhnacademy.shoppingmall.address.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Address {
    private final String addressId;
    private String userId;
    private String addressName;
    private String zipCode;
    private String roadAddress;
    private String detailAddress;
    private boolean defaultAddress;
}
