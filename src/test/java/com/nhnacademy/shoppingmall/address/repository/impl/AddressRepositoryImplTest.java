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
    void findById() {
        Optional<Address> address = addressRepository.findById(testAddress.getAddressId());
        assertTrue(address.isPresent());
        assertEquals(testAddress.getAddressId(), address.get().getAddressId());
    }

    @Test
    @Order(2)
    void findByUserId() {
        var addresses = addressRepository.findByUserId(testUser.getUserId());
        assertFalse(addresses.isEmpty());
        assertEquals(1, addresses.size());
    }

    @Test
    @Order(3)
    void update() {
        testAddress.setAddressName("Office");
        addressRepository.update(testAddress);

        Optional<Address> updated = addressRepository.findById(testAddress.getAddressId());
        assertTrue(updated.isPresent());
        assertEquals("Office", updated.get().getAddressName());
    }

    @Test
    @Order(4)
    void deleteById() {
        addressRepository.deleteById(testAddress.getAddressId());
        Optional<Address> address = addressRepository.findById(testAddress.getAddressId());
        assertFalse(address.isPresent());
    }
}
