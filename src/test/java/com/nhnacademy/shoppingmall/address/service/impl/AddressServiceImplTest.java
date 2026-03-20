package com.nhnacademy.shoppingmall.address.service.impl;

import com.nhnacademy.shoppingmall.address.domain.Address;
import com.nhnacademy.shoppingmall.address.exception.AddressAlreadyExistsException;
import com.nhnacademy.shoppingmall.address.exception.AddressNotFoundException;
import com.nhnacademy.shoppingmall.address.repository.AddressRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class AddressServiceImplTest {

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    AddressServiceImpl addressService;

    @Test
    @Order(1)
    @DisplayName("getAddress - 성공")
    void getAddress() {
        Address address = new Address("addr-1", "user-1", "Home", "12345", "Road", "Detail", true);
        when(addressRepository.findById("addr-1")).thenReturn(Optional.of(address));

        Address result = addressService.getAddress("addr-1");

        assertNotNull(result);
        assertEquals("addr-1", result.getAddressId());
        verify(addressRepository, times(1)).findById("addr-1");
    }

    @Test
    @Order(2)
    @DisplayName("getAddress - 실패 (주소 없음)")
    void getAddress_notFound() {
        when(addressRepository.findById("addr-1")).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> addressService.getAddress("addr-1"));
    }

    @Test
    @Order(3)
    @DisplayName("saveAddress - 성공 (기본 배송지로 추가 시 기존 기본 배송지 초기화)")
    void saveAddress_withDefault() {
        Address address = new Address("addr-1", "user-1", "Home", "12345", "Road", "Detail", true);
        when(addressRepository.findById("addr-1")).thenReturn(Optional.empty());
        when(addressRepository.countByUserId("user-1")).thenReturn(1);

        addressService.saveAddress(address);

        verify(addressRepository, times(1)).resetDefaultAddress("user-1", "addr-1");
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    @Order(4)
    @DisplayName("saveAddress - 성공 (첫 주소 추가 시 자동으로 기본 배송지 설정)")
    void saveAddress_firstAddress() {
        Address address = new Address("addr-1", "user-1", "Home", "12345", "Road", "Detail", false);
        when(addressRepository.findById("addr-1")).thenReturn(Optional.empty());
        when(addressRepository.countByUserId("user-1")).thenReturn(0);

        addressService.saveAddress(address);

        assertTrue(address.isDefaultAddress());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    @Order(5)
    @DisplayName("saveAddress - 실패 (이미 존재하는 주소 ID)")
    void saveAddress_alreadyExists() {
        Address address = new Address("addr-1", "user-1", "Home", "12345", "Road", "Detail", false);
        when(addressRepository.findById("addr-1")).thenReturn(Optional.of(address));

        assertThrows(AddressAlreadyExistsException.class, () -> addressService.saveAddress(address));
    }

    @Test
    @Order(6)
    @DisplayName("updateAddress - 성공 (기본 배송지로 변경 시 기존 기본 배송지 초기화)")
    void updateAddress() {
        Address address = new Address("addr-1", "user-1", "Home", "12345", "Road", "Detail", true);
        when(addressRepository.findById("addr-1")).thenReturn(Optional.of(address));

        addressService.updateAddress(address);

        verify(addressRepository, times(1)).resetDefaultAddress("user-1", "addr-1");
        verify(addressRepository, times(1)).update(address);
    }

    @Test
    @Order(7)
    @DisplayName("updateAddress - 실패 (주소 없음)")
    void updateAddress_notFound() {
        Address address = new Address("addr-1", "user-1", "Home", "12345", "Road", "Detail", true);
        when(addressRepository.findById("addr-1")).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> addressService.updateAddress(address));
    }

    @Test
    @Order(8)
    @DisplayName("deleteAddress - 성공 (일반 배송지 삭제)")
    void deleteAddress() {
        Address address = new Address("addr-1", "user-1", "Home", "12345", "Road", "Detail", false);
        when(addressRepository.findById("addr-1")).thenReturn(Optional.of(address));

        addressService.deleteAddress("addr-1");

        verify(addressRepository, times(1)).deleteById("addr-1");
    }

    @Test
    @Order(9)
    @DisplayName("deleteAddress - 성공 (기본 배송지 삭제 시 다른 배송지를 기본으로 설정)")
    void deleteAddress_defaultAddress() {
        Address address = new Address("addr-1", "user-1", "Home", "12345", "Road", "Detail", true);
        when(addressRepository.findById("addr-1")).thenReturn(Optional.of(address));

        Address otherAddr = new Address("addr-2", "user-1", "Office", "54321", "Road2", "Detail2", false);
        when(addressRepository.findByUserId("user-1")).thenReturn(List.of(otherAddr));

        addressService.deleteAddress("addr-1");

        assertTrue(otherAddr.isDefaultAddress());
        verify(addressRepository, times(1)).update(otherAddr);
        verify(addressRepository, times(1)).deleteById("addr-1");
    }

    @Test
    @Order(10)
    @DisplayName("deleteAddress - 실패 (주소 없음)")
    void deleteAddress_notFound() {
        when(addressRepository.findById("addr-1")).thenReturn(Optional.empty());

        assertThrows(AddressNotFoundException.class, () -> addressService.deleteAddress("addr-1"));
    }

    @Test
    @Order(11)
    @DisplayName("getAddressesByUserId - 성공")
    void getAddressesByUserId() {
        when(addressRepository.findByUserId("user-1")).thenReturn(List.of(
                new Address("addr-1", "user-1", "Home", "12345", "Road", "Detail", true),
                new Address("addr-2", "user-1", "Office", "54321", "Road2", "Detail2", false)
        ));

        List<Address> result = addressService.getAddressesByUserId("user-1");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(addressRepository, times(1)).findByUserId("user-1");
    }
}
