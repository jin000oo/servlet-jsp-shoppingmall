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
            // 상세 이미지 정보 DB 저장
            insertImageMappings(connection, product.getProductId(), product.getDetailImagePaths());
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

            // Product 수정 시, 이미지 정보 재등록
            deleteImageMappings(connection, product.getProductId());
            insertImageMappings(connection, product.getProductId(), product.getDetailImagePaths());
            
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> findById(String productId) {
        String sql = """
                SELECT p.product_id, p.product_name, p.price, p.stock, p.thumbnail_image_path,
                       (SELECT GROUP_CONCAT(category_id) FROM product_category WHERE product_id = p.product_id) as category_ids,
                       (SELECT GROUP_CONCAT(image_path) FROM images WHERE product_id = p.product_id) as detail_image_paths
                FROM products p
                WHERE p.product_id = ?
                """;
        Connection connection = DbConnectionThreadLocal.getConnection();
        try(PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, productId);

            ResultSet rs = psmt.executeQuery();
            if(rs.next()) {
                return Optional.of(mapRowToProduct(rs));
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
                SELECT p.product_id, p.product_name, p.price, p.stock, p.thumbnail_image_path,
                       (SELECT GROUP_CONCAT(category_id) FROM product_category WHERE product_id = p.product_id) as category_ids,
                       (SELECT GROUP_CONCAT(image_path) FROM images WHERE product_id = p.product_id) as detail_image_paths
                FROM products p 
                ORDER BY p.product_id DESC LIMIT ? OFFSET ?
                """;
        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
            selectStmt.setInt(1, pageSize);
            selectStmt.setInt(2, offset);
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    content.add(mapRowToProduct(rs));
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
                JOIN product_category pc ON p.product_id = pc.product_id 
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
                SELECT p.product_id, p.product_name, p.price, p.stock, p.thumbnail_image_path,
                       (SELECT GROUP_CONCAT(category_id) FROM product_category WHERE product_id = p.product_id) as category_ids,
                       (SELECT GROUP_CONCAT(image_path) FROM images WHERE product_id = p.product_id) as detail_image_paths
                FROM products p 
                JOIN product_category pc ON p.product_id = pc.product_id 
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
                    content.add(mapRowToProduct(rs));
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
        deleteImageMappings(connection, productId);
        
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

    private void insertImageMappings(Connection conn, String productId, List<String> imagePaths) {
        if (imagePaths == null || imagePaths.isEmpty()) return;
        String sql = "INSERT INTO images(product_id, image_path) VALUES(?, ?)";
        try (PreparedStatement psmt = conn.prepareStatement(sql)) {
            for (String imagePath : imagePaths) {
                psmt.setString(1, productId);
                psmt.setString(2, imagePath);
                psmt.addBatch();
            }
            psmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteImageMappings(Connection conn, String productId) {
        String sql = "DELETE FROM images WHERE product_id = ?";
        try (PreparedStatement psmt = conn.prepareStatement(sql)) {
            psmt.setString(1, productId);
            psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private Product mapRowToProduct(ResultSet rs) throws SQLException {
        String productId = rs.getString("product_id");
        String categoryIdsStr = rs.getString("category_ids");
        List<String> categoryIds = new ArrayList<>();
        if (categoryIdsStr != null && !categoryIdsStr.isBlank()) {
            categoryIds.addAll(List.of(categoryIdsStr.split(",")));
        }

        Product product = new Product(
                productId,
                rs.getString("product_name"),
                rs.getInt("price"),
                rs.getInt("stock"),
                rs.getString("thumbnail_image_path"),
                categoryIds
        );

        String detailImagePathsStr = rs.getString("detail_image_paths");
        List<String> detailImagePaths = new ArrayList<>();
        if (detailImagePathsStr != null && !detailImagePathsStr.isBlank()) {
            detailImagePaths.addAll(List.of(detailImagePathsStr.split(",")));
        }
        product.setDetailImagePaths(detailImagePaths);

        return product;
    }
}