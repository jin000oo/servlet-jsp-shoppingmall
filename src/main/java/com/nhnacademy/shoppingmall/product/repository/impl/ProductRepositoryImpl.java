package com.nhnacademy.shoppingmall.product.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public int save(Product product) {
        String sql = """
                INSERT INTO products(product_id, product_name, price, stock, thumbnail_image_path)
                VALUES(?, ?, ?, ?, ?)
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, product.getProductId());
            psmt.setString(2, product.getProductName());
            psmt.setInt(3, product.getPrice());
            psmt.setInt(4, product.getStock());
            psmt.setString(5, product.getThumbnailImagePath());

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Product product) {
        String sql = """
                UPDATE products
                SET product_name = ?, price = ?, stock = ?, thumbnail_image_path = ?
                WHERE product_id = ?
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, product.getProductName());
            psmt.setInt(2, product.getPrice());
            psmt.setInt(3, product.getStock());
            psmt.setString(4, product.getThumbnailImagePath());
            psmt.setString(5, product.getProductId());

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> findById(String productId) {
        String sql = """
                SELECT product_name, price, stock, thumbnail_image_path
                FROM products
                WHERE product_id = ?
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, productId);

            ResultSet rs = psmt.executeQuery();
            if(rs.next()) {
                Product product = new Product(
                        productId,
                        rs.getString("product_name"),
                        rs.getInt("price"),
                        rs.getInt("stock"),
                        rs.getString("thumbnail_image_path")
                );
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Page<Product> findAll(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        long totalCount = 0;
        List<Product> content = new ArrayList<>();

        Connection connection = DbConnectionThreadLocal.getConnection();

        String countSql = "SELECT COUNT(*) FROM products";
        try (PreparedStatement countStmt = connection.prepareStatement(countSql);
             ResultSet countRs = countStmt.executeQuery()) {
            if (countRs.next()) {
                totalCount = countRs.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String selectSql = """
                SELECT product_id, product_name, price, stock, thumbnail_image_path 
                FROM products 
                ORDER BY product_id DESC LIMIT ? OFFSET ?
                """;
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setInt(1, pageSize);
            selectStmt.setInt(2, offset);
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    content.add(new Product(
                            rs.getString("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("price"),
                            rs.getInt("stock"),
                            rs.getString("thumbnail_image_path")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Page<>(content, totalCount);
    }

    @Override
    public Page<Product> findByCategory(String categoryId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        long totalCount = 0;
        List<Product> content = new ArrayList<>();

        Connection connection = DbConnectionThreadLocal.getConnection();

        String countSql = """
                SELECT COUNT(*) 
                FROM products p 
                INNER JOIN product_category pc ON p.product_id = pc.product_id 
                WHERE pc.category_id = ?
                """;
        try (PreparedStatement countStmt = connection.prepareStatement(countSql)) {
            countStmt.setString(1, categoryId);
            try (ResultSet countRs = countStmt.executeQuery()) {
                if (countRs.next()) {
                    totalCount = countRs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String selectSql = """
                SELECT p.product_id, p.product_name, p.price, p.stock, p.thumbnail_image_path
                FROM products p 
                INNER JOIN product_category pc ON p.product_id = pc.product_id 
                WHERE pc.category_id = ? 
                ORDER BY p.product_id DESC 
                LIMIT ? OFFSET ?
                """;
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setString(1, categoryId);
            selectStmt.setInt(2, pageSize);
            selectStmt.setInt(3, offset);
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    content.add(new Product(
                            rs.getString("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("price"),
                            rs.getInt("stock"),
                            rs.getString("thumbnail_image_path")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Page<>(content, totalCount);
    }

    /*
    @Override
    public Page<Product> findByProductName(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        long totalCount = 0;
        List<Product> content = new ArrayList<>();

        Connection connection = DbConnectionThreadLocal.getConnection();

        String countSql = "SELECT COUNT(*) FROM products WHERE product_name LIKE ?";
        try (PreparedStatement countStmt = connection.prepareStatement(countSql)) {
            countStmt.setString(1, "%" + keyword + "%");
            try (ResultSet countRs = countStmt.executeQuery()) {
                if (countRs.next()) {
                    totalCount = countRs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String selectSql = """
                SELECT product_id, product_name, price, stock, thumbnail_image_path 
                FROM products 
                WHERE product_name LIKE ?
                ORDER BY product_id DESC LIMIT ? OFFSET ?
                """;
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setString(1, "%" + keyword + "%");
            selectStmt.setInt(2, pageSize);
            selectStmt.setInt(3, offset);
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    content.add(new Product(
                            rs.getString("product_id"),
                            rs.getString("product_name"),
                            rs.getInt("price"),
                            rs.getInt("stock"),
                            rs.getString("thumbnail_image_path")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Page<>(content, totalCount);
    }
    */

    @Override
    public int saveProductCategory(String productId, String categoryId) {
        String sql = "INSERT INTO product_category(product_id, category_id) VALUES(?, ?)";
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, productId);
            psmt.setString(2, categoryId);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteCategoriesByProductId(String productId) {
        String sql = "DELETE FROM product_category WHERE product_id = ?";
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, productId);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteById(String productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, productId);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countById(String productId) {
        String sql = "SELECT COUNT(*) FROM products WHERE product_id = ?";

        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, productId);
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
