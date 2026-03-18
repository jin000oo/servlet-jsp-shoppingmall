package com.nhnacademy.shoppingmall.address.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Address {

    private final String addressId;
    private String userId;
    private String addressName;
    private String zipCode;
    private String roadAddress;
    private String detailAddress;
    private boolean isDefault;

    public Address(String addressId, String userId, String addressName, String zipCode, String roadAddress, String detailAddress, boolean isDefault) {
        this.addressId = addressId;
        this.userId = userId;
        this.addressName = addressName;
        this.zipCode = zipCode;
        this.roadAddress = roadAddress;
        this.detailAddress = detailAddress;
        this.isDefault = isDefault;
    }
}
