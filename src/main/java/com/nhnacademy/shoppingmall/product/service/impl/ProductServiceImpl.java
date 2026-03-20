package com.nhnacademy.shoppingmall.product.service.impl;

import com.nhnacademy.shoppingmall.common.mvc.annotation.Service;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.exception.CategoryNotFoundException;
import com.nhnacademy.shoppingmall.product.exception.ProductAlreadyExistsException;
import com.nhnacademy.shoppingmall.product.exception.ProductNotFoundException;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.service.ProductService;

import java.util.List;

@Service(ProductService.CONTEXT_PRODUCT_SERVICE_NAME)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getProduct(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public void saveProduct(Product product) {
        if (productRepository.existsById(product.getProductId())) {
            throw new ProductAlreadyExistsException(product.getProductId());
        }

        // Verify all category IDs exist in DB using a single query
        List<String> categoryIds = product.getCategoryIds();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            if (categoryRepository.findByIds(categoryIds).size() != categoryIds.size()) {
                throw new CategoryNotFoundException("One or more categories not found");
            }
        }

        int result = productRepository.save(product);
        if (result < 1) {
            throw new RuntimeException("Failed to register product: " + product.getProductId());
        }
    }

    @Override
    public void updateProduct(Product product) {
        if (!productRepository.existsById(product.getProductId())) {
            throw new ProductNotFoundException(product.getProductId());
        }

        List<String> categoryIds = product.getCategoryIds();
        if (categoryIds != null && !categoryIds.isEmpty()) {
            if (categoryRepository.findByIds(categoryIds).size() != categoryIds.size()) {
                throw new CategoryNotFoundException("One or more categories not found");
            }
        }

        int result = productRepository.update(product);
        if (result < 1) {
            throw new RuntimeException("Failed to modify product: " + product.getProductId());
        }
    }

    @Override
    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException(productId);
        }

        int result = productRepository.deleteById(productId);
        if (result < 1) {
            throw new RuntimeException("Failed to delete product: " + productId);
        }
    }

    @Override
    public Page<Product> getProducts(int page, int pageSize) {
        if(page <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("page or pageSize must be a positive number");
        }
        return productRepository.findAll(page, pageSize);
    }

    @Override
    public Page<Product> getProductsByCategory(String categoryId, int page, int pageSize) {
        return productRepository.findByCategory(categoryId, page, pageSize);
    }

    @Override
    public Page<Product> getProductsByName(String name, int page, int pageSize) {
        if (name == null || name.isBlank()) {
            return getProducts(page, pageSize);
        }
        return productRepository.findByName(name, page, pageSize);
    }

    @Override
    public List<Product> getProductsByIds(List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }
        return productRepository.findByIds(productIds);
    }
}
