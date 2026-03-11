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

            int result = psmt.executeUpdate();

            // DB의 product_category 에 매핑
            insertCategoryMappings(connection, product.getProductId(), product.getCategoryIds());
            return result;
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

            int result = psmt.executeUpdate();

            // Product 수정 시, 카테고리 매핑 재등록
            deleteCategoryMappings(connection, product.getProductId());
            insertCategoryMappings(connection, product.getProductId(), product.getCategoryIds());
            
            return result;
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
                List<String> categoryIds = getCategoryIdsByProductId(connection, productId);
                Product product = new Product(
                        productId,
                        rs.getString("product_name"),
                        rs.getInt("price"),
                        rs.getInt("stock"),
                        rs.getString("thumbnail_image_path"),
                        categoryIds
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

        // get products count
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
                    String productId = rs.getString("product_id");
                    List<String> categoryIds = getCategoryIdsByProductId(connection, productId);
                    content.add(new Product(
                            productId,
                            rs.getString("product_name"),
                            rs.getInt("price"),
                            rs.getInt("stock"),
                            rs.getString("thumbnail_image_path"),
                            categoryIds
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

        // get product count by category
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
                    String productId = rs.getString("product_id");
                    List<String> categoryIds = getCategoryIdsByProductId(connection, productId);
                    content.add(new Product(
                            productId,
                            rs.getString("product_name"),
                            rs.getInt("price"),
                            rs.getInt("stock"),
                            rs.getString("thumbnail_image_path"),
                            categoryIds
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Page<>(content, totalCount);
    }

    @Override
    public int deleteById(String productId) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        deleteCategoryMappings(connection, productId);
        
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, productId);
            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(String productId) {
        String sql = "SELECT 1 FROM products WHERE product_id = ? LIMIT 1";

        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, productId);
            try (ResultSet rs = psmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    // Private Helper Methods
    // product 등록 시, DB의 product_category에 카테고리들 매핑.
    private void insertCategoryMappings(Connection conn, String productId, List<String> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) return;
        String sql = "INSERT INTO product_category(product_id, category_id) VALUES(?, ?)";
        try (PreparedStatement psmt = conn.prepareStatement(sql)) {
            for (String categoryId : categoryIds) {
                psmt.setString(1, productId);
                psmt.setString(2, categoryId);
                psmt.addBatch();
            }
            psmt.executeBatch();
        } catch (SQLException e) { 
            throw new RuntimeException(e); 
        }
    }

    private void deleteCategoryMappings(Connection conn, String productId) {
        String sql = "DELETE FROM product_category WHERE product_id = ?";
        try (PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, productId);
            psmt.executeUpdate();
        } catch (SQLException e) { 
            throw new RuntimeException(e); 
        }
    }
    
    private List<String> getCategoryIdsByProductId(Connection conn, String productId) {
        List<String> categoryIds = new ArrayList<>();
        String sql = "SELECT category_id FROM product_category WHERE product_id = ?";
        try (PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, productId);
            try(ResultSet rs = psmt.executeQuery()) {
                while(rs.next()) {
                    categoryIds.add(rs.getString("category_id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categoryIds;
    }
}