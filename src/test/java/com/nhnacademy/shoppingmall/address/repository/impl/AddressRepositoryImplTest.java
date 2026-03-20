package com.nhnacademy.shoppingmall.address.repository.impl;

import com.nhnacademy.shoppingmall.address.domain.Address;
import com.nhnacademy.shoppingmall.address.repository.AddressRepository;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AddressRepositoryImplTest {

    UserRepository userRepository = new UserRepositoryImpl();
    AddressRepository addressRepository = new AddressRepositoryImpl();
    User testUser;
    Address testAddress;

    @BeforeEach
    void setUp() throws SQLException {
        DbConnectionThreadLocal.initialize();

        testUser = new User(
                "test-address-user",
                "test-name",
                "pwd",
                "19900101",
                User.Auth.ROLE_USER,
                10000,
                LocalDateTime.now(),
                null
        );
        userRepository.save(testUser);

        testAddress = new Address(
                "addr-id-1",
                testUser.getUserId(),
                "Home",
                "12345",
                "Seoul Road",
                "Apt 1",
                true
        );
        addressRepository.save(testAddress);
    }

    @AfterEach
    void tearDown() throws SQLException {
        DbConnectionThreadLocal.setSqlError(true);
        DbConnectionThreadLocal.reset();
    }

    @Test
    @Order(1)
    @DisplayName("주소 아이디로 조회")
    void findById() {
        Optional<Address> address = addressRepository.findById(testAddress.getAddressId());
        assertTrue(address.isPresent());
        assertEquals(testAddress.getAddressId(), address.get().getAddressId());
    }

    @Test
    @Order(2)
    @DisplayName("사용자 아이디로 모든 주소 조회")
    void findByUserId() {
        List<Address> addresses = addressRepository.findByUserId(testUser.getUserId());
        assertFalse(addresses.isEmpty());
        assertEquals(1, addresses.size());
    }

    @Test
    @Order(3)
    @DisplayName("주소 정보 수정")
    void update() {
        testAddress.setAddressName("Office");
        addressRepository.update(testAddress);

        Optional<Address> updated = addressRepository.findById(testAddress.getAddressId());
        assertTrue(updated.isPresent());
        assertEquals("Office", updated.get().getAddressName());
    }

    @Test
    @Order(4)
    @DisplayName("주소 삭제")
    void deleteById() {
        addressRepository.deleteById(testAddress.getAddressId());
        Optional<Address> address = addressRepository.findById(testAddress.getAddressId());
        assertFalse(address.isPresent());
    }

    @Test
    @Order(5)
    @DisplayName("사용자의 모든 주소 삭제")
    void deleteByUserId() {
        addressRepository.deleteByUserId(testUser.getUserId());
        List<Address> addresses = addressRepository.findByUserId(testUser.getUserId());
        assertTrue(addresses.isEmpty());
    }

    @Test
    @Order(6)
    @DisplayName("사용자의 주소 개수 카운트")
    void countByUserId() {
        int count = addressRepository.countByUserId(testUser.getUserId());
        assertEquals(1, count);

        Address secondAddress = new Address(
                "addr-id-2",
                testUser.getUserId(),
                "Office",
                "54321",
                "Gyeonggi Road",
                "Apt 2",
                false
        );
        addressRepository.save(secondAddress);
        assertEquals(2, addressRepository.countByUserId(testUser.getUserId()));
    }

    @Test
    @Order(7)
    @DisplayName("기본 배송지 초기화 (모두 false로)")
    void resetDefaultAddress() {
        // 기존 1개는 true인 상태
        addressRepository.resetDefaultAddress(testUser.getUserId(), testAddress.getAddressId());
        
        Optional<Address> address = addressRepository.findById(testAddress.getAddressId());
        assertTrue(address.isPresent());
        assertFalse(address.get().isDefaultAddress());
    }
}
