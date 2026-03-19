package com.nhnacademy.shoppingmall.address.service.impl;

import com.nhnacademy.shoppingmall.address.domain.Address;
import com.nhnacademy.shoppingmall.address.exception.AddressAlreadyExistsException;
import com.nhnacademy.shoppingmall.address.exception.AddressNotFoundException;
import com.nhnacademy.shoppingmall.address.repository.AddressRepository;
import com.nhnacademy.shoppingmall.address.service.AddressService;

import java.util.List;

public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getAddress(String addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
    }

    @Override
    public void saveAddress(Address address) {
        if (addressRepository.findById(address.getAddressId()).isPresent()) {
            throw new AddressAlreadyExistsException(address.getAddressId());
        }

        // 처음 등록하는 주소면, defaultAddress = true
        if (addressRepository.countByUserId(address.getUserId()) == 0) {
            address.setDefaultAddress(true);
        } else if (address.isDefaultAddress()) {
            // 기본 주소로 등록되어 있는 경우, 다른 주소의 defaultAddress = false
            resetDefaultAddress(address.getUserId(), address.getAddressId());
        }

        addressRepository.save(address);
    }

    @Override
    public void updateAddress(Address address) {
        if (addressRepository.findById(address.getAddressId()).isEmpty()) {
            throw new AddressNotFoundException(address.getAddressId());
        }

        if (address.isDefaultAddress()) {
            resetDefaultAddress(address.getUserId(), address.getAddressId());
        }

        addressRepository.update(address);
    }

    @Override
    public void deleteAddress(String addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new AddressNotFoundException(addressId));
        
        addressRepository.deleteById(addressId);

        // 삭제하는 값이 default 주소이면, 다른 주소 하나를 새로 defaultAddress로 설정
        if (address.isDefaultAddress()) {
            List<Address> addresses = addressRepository.findByUserId(address.getUserId());
            if (!addresses.isEmpty()) {
                Address newDefault = addresses.get(0);
                newDefault.setDefaultAddress(true);
                addressRepository.update(newDefault);
            }
        }
    }

    @Override
    public List<Address> getAddressesByUserId(String userId) {
        return addressRepository.findByUserId(userId);
    }

    private void resetDefaultAddress(String userId, String addressId) {
        addressRepository.resetDefaultAddress(userId, addressId);
    }
}
