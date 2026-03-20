package com.nhnacademy.shoppingmall.address.repository.impl;

import com.nhnacademy.shoppingmall.address.domain.Address;
import com.nhnacademy.shoppingmall.address.repository.AddressRepository;
import com.nhnacademy.shoppingmall.common.mvc.annotation.Repository;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    @Override
    public Optional<Address> findById(String addressId) {
        String sql = "SELECT * FROM addresses WHERE address_id = ?";
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, addressId);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    Address address = new Address(
                            rs.getString("address_id"),
                            rs.getString("user_id"),
                            rs.getString("address_name"),
                            rs.getString("zip_code"),
                            rs.getString("road_address"),
                            rs.getString("detail_address"),
                            rs.getBoolean("is_default")
                    );
                    return Optional.of(address);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Address> findByUserId(String userId) {
        String sql = "SELECT * FROM addresses WHERE user_id = ?";
        Connection connection = DbConnectionThreadLocal.getConnection();
        List<Address> addresses = new ArrayList<>();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    addresses.add(new Address(
                            rs.getString("address_id"),
                            rs.getString("user_id"),
                            rs.getString("address_name"),
                            rs.getString("zip_code"),
                            rs.getString("road_address"),
                            rs.getString("detail_address"),
                            rs.getBoolean("is_default")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return addresses;
    }

    @Override
    public int save(Address address) {
        String sql = """
                INSERT INTO addresses (address_id, user_id, address_name, zip_code, road_address, detail_address, is_default)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, address.getAddressId());
            psmt.setString(2, address.getUserId());
            psmt.setString(3, address.getAddressName());
            psmt.setString(4, address.getZipCode());
            psmt.setString(5, address.getRoadAddress());
            psmt.setString(6, address.getDetailAddress());
            psmt.setBoolean(7, address.isDefaultAddress());
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Address address) {
        String sql = """
                UPDATE addresses
                SET user_id = ?,
                    address_name = ?,
                    zip_code = ?,
                    road_address = ?,
                    detail_address = ?,
                    is_default = ?
                WHERE address_id = ?
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, address.getUserId());
            psmt.setString(2, address.getAddressName());
            psmt.setString(3, address.getZipCode());
            psmt.setString(4, address.getRoadAddress());
            psmt.setString(5, address.getDetailAddress());
            psmt.setBoolean(6, address.isDefaultAddress());
            psmt.setString(7, address.getAddressId());
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String addressId) {
        String sql = "DELETE FROM addresses WHERE address_id = ?";
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, addressId);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByUserId(String userId) {
        String sql = "DELETE FROM addresses WHERE user_id = ?";
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByUserId(String userId) {
        String sql = "SELECT COUNT(*) FROM addresses WHERE user_id = ?";
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int resetDefaultAddress(String userId, String addressId) {
        String sql = "UPDATE addresses SET is_default = 0 WHERE user_id = ?";

        Connection connection = DbConnectionThreadLocal.getConnection();
        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
