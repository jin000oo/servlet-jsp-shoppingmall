package com.nhnacademy.shoppingmall.product.service.impl;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.exception.CategoryNotFoundException;
import com.nhnacademy.shoppingmall.product.exception.ProductAlreadyExistsException;
import com.nhnacademy.shoppingmall.product.exception.ProductNotFoundException;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.service.ProductService;

import java.util.List;

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
    public void registerProduct(Product product, List<String> categoryIds) {
        if (productRepository.countById(product.getProductId()) > 0) {
            throw new ProductAlreadyExistsException(product.getProductId());
        }

        validateCategories(categoryIds);

        int result = productRepository.save(product);
        if (result < 1) {
            throw new RuntimeException("Failed to register product: " + product.getProductId());
        }

        saveProductCategories(product.getProductId(), categoryIds);
    }

    @Override
    public void modifyProduct(Product product, List<String> categoryIds) {
        if (productRepository.countById(product.getProductId()) == 0) {
            throw new ProductNotFoundException(product.getProductId());
        }

        validateCategories(categoryIds);

        int result = productRepository.update(product);
        if (result < 1) {
            throw new RuntimeException("Failed to modify product: " + product.getProductId());
        }

        productRepository.deleteCategoriesByProductId(product.getProductId());
        saveProductCategories(product.getProductId(), categoryIds);
    }

    @Override
    public void deleteProduct(String productId) {
        if (productRepository.countById(productId) == 0) {
            throw new ProductNotFoundException(productId);
        }

        productRepository.deleteCategoriesByProductId(productId);
        int result = productRepository.deleteById(productId);
        if (result < 1) {
            throw new RuntimeException("Failed to delete product: " + productId);
        }
    }

    @Override
    public Page<Product> getProducts(int page, int pageSize) {
        return productRepository.findAll(page, pageSize);
    }

    @Override
    public Page<Product> getProductsByCategory(String categoryId, int page, int pageSize) {
        return productRepository.findByCategory(categoryId, page, pageSize);
    }

    private void validateCategories(List<String> categoryIds) {
        // 상품은 반드시 1개 이상 최대 3개의 카테고리를 가져야 함.
        if (categoryIds != null && !categoryIds.isEmpty()) {
            if (categoryIds.size() > 3) {
                throw new IllegalArgumentException("A product can belong to a maximum of 3 categories.");
            }
            for (String categoryId : categoryIds) {
                if (categoryRepository.countById(categoryId) == 0) {
                    throw new CategoryNotFoundException(categoryId);
                }
            }
        }
    }

    private void saveProductCategories(String productId, List<String> categoryIds) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            for (String categoryId : categoryIds) {
                productRepository.saveProductCategory(productId, categoryId);
            }
        }
    }
}
