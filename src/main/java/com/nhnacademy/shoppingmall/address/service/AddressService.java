package com.nhnacademy.shoppingmall.address.service;

import com.nhnacademy.shoppingmall.address.domain.Address;

import java.util.List;

public interface AddressService {
    String CONTEXT_ADDRESS_SERVICE_NAME = "CONTEXT_ADDRESS_SERVICE";

    Address getAddress(String addressId);
    void saveAddress(Address address);
    void updateAddress(Address address);
    void deleteAddress(String addressId);
    List<Address> getAddressesByUserId(String userId);
}
