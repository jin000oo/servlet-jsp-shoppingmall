package com.nhnacademy.shoppingmall.product.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository {

    @Override
    public int save(Category category) {
        String sql = """
                INSERT INTO categories(category_id, category_name, sort_order)
                VALUES(?, ?, ?)
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, category.getCategoryId());
            psmt.setString(2, category.getCategoryName());
            psmt.setInt(3, category.getSortOrder());
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Category category) {
        String sql = """
                UPDATE categories
                SET category_name = ?, sort_order = ?
                WHERE category_id = ?
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, category.getCategoryName());
            psmt.setInt(2, category.getSortOrder());
            psmt.setString(3, category.getCategoryId());
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Category> findById(String categoryId) {
        String sql = """
                SELECT category_name, sort_order
                FROM categories
                WHERE category_id = ?
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, categoryId);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Category(
                            categoryId,
                            rs.getString("category_name"),
                            rs.getInt("sort_order")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public List<Category> findAll() {
        String sql = """
                SELECT category_id, category_name, sort_order
                FROM categories
                ORDER BY sort_order ASC, category_id ASC
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement psmt = connection.prepareStatement(sql);
             ResultSet rs = psmt.executeQuery()) {
            while (rs.next()) {
                categories.add(new Category(
                        rs.getString("category_id"),
                        rs.getString("category_name"),
                        rs.getInt("sort_order")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }

    @Override
    public int deleteById(String categoryId) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, categoryId);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countById(String categoryId) {
        String sql = "SELECT COUNT(*) FROM categories WHERE category_id = ?";
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, categoryId);
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
}

