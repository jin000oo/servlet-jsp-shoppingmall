package com.nhnacademy.shoppingmall.product.service.impl;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.exception.CategoryNotFoundException;
import com.nhnacademy.shoppingmall.product.exception.ProductAlreadyExistsException;
import com.nhnacademy.shoppingmall.product.exception.ProductNotFoundException;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
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
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    ProductServiceImpl productService;

    @Test
    @Order(1)
    @DisplayName("getProduct - 성공")
    void getProduct() {
        // given
        Product product = new Product("P001", "상품1", 10000, 10, List.of("C001"));
        when(productRepository.findById("P001")).thenReturn(Optional.of(product));

        // when
        Product result = productService.getProduct("P001");

        // then
        assertNotNull(result);
        assertEquals("P001", result.getProductId());
        verify(productRepository, times(1)).findById("P001");
    }

    @Test
    @Order(2)
    @DisplayName("getProduct - 실패 (상품 없음)")
    void getProduct_notFound() {
        // given
        when(productRepository.findById("P001")).thenReturn(Optional.empty());

        // when, then
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProduct("P001"));
        assertTrue(exception.getMessage().contains("P001"));
    }

    @Test
    @Order(3)
    @DisplayName("saveProduct - 성공")
    void saveProduct() {
        // given
        Product product = new Product("P001", "상품1", 10000, 10, List.of("C001"));
        when(productRepository.existsById("P001")).thenReturn(false);
        when(categoryRepository.existsById("C001")).thenReturn(true);
        when(productRepository.save(product)).thenReturn(1);

        // when
        productService.saveProduct(product);

        // then
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @Order(4)
    @DisplayName("saveProduct - 실패 (이미 존재하는 상품)")
    void saveProduct_alreadyExists() {
        // given
        Product product = new Product("P001", "상품1", 10000, 10, List.of("C001"));
        when(productRepository.existsById("P001")).thenReturn(true);

        // when, then
        assertThrows(ProductAlreadyExistsException.class, () -> productService.saveProduct(product));
    }

    @Test
    @Order(5)
    @DisplayName("saveProduct - 실패 (카테고리 없음)")
    void saveProduct_categoryNotFound() {
        // given
        Product product = new Product("P001", "상품1", 10000, 10, List.of("C001"));
        when(productRepository.existsById("P001")).thenReturn(false);
        when(categoryRepository.existsById("C001")).thenReturn(false);

        // when, then
        assertThrows(CategoryNotFoundException.class, () -> productService.saveProduct(product));
    }

    @Test
    @Order(6)
    @DisplayName("saveProduct - 실패 (저장 실패)")
    void saveProduct_failToSave() {
        // given
        Product product = new Product("P001", "상품1", 10000, 10, List.of("C001"));
        when(productRepository.existsById("P001")).thenReturn(false);
        when(categoryRepository.existsById("C001")).thenReturn(true);
        when(productRepository.save(product)).thenReturn(0);

        // when, then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.saveProduct(product));
        assertTrue(exception.getMessage().contains("Failed to register product"));
    }

    @Test
    @Order(7)
    @DisplayName("updateProduct - 성공")
    void updateProduct() {
        // given
        Product product = new Product("P001", "상품1", 10000, 10, List.of("C001"));
        when(productRepository.existsById("P001")).thenReturn(true);
        when(categoryRepository.existsById("C001")).thenReturn(true);
        when(productRepository.update(product)).thenReturn(1);

        // when
        productService.updateProduct(product);

        // then
        verify(productRepository, times(1)).update(product);
    }

    @Test
    @Order(8)
    @DisplayName("updateProduct - 실패 (상품 없음)")
    void updateProduct_notFound() {
        // given
        Product product = new Product("P001", "상품1", 10000, 10, List.of("C001"));
        when(productRepository.existsById("P001")).thenReturn(false);

        // when, then
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(product));
    }

    @Test
    @Order(9)
    @DisplayName("updateProduct - 실패 (카테고리 없음)")
    void updateProduct_categoryNotFound() {
        // given
        Product product = new Product("P001", "상품1", 10000, 10, List.of("C001"));
        when(productRepository.existsById("P001")).thenReturn(true);
        when(categoryRepository.existsById("C001")).thenReturn(false);

        // when, then
        assertThrows(CategoryNotFoundException.class, () -> productService.updateProduct(product));
    }

    @Test
    @Order(10)
    @DisplayName("updateProduct - 실패 (수정 실패)")
    void updateProduct_failToUpdate() {
        // given
        Product product = new Product("P001", "상품1", 10000, 10, List.of("C001"));
        when(productRepository.existsById("P001")).thenReturn(true);
        when(categoryRepository.existsById("C001")).thenReturn(true);
        when(productRepository.update(product)).thenReturn(0);

        // when, then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.updateProduct(product));
        assertTrue(exception.getMessage().contains("Failed to modify product"));
    }

    @Test
    @Order(11)
    @DisplayName("deleteProduct - 성공")
    void deleteProduct() {
        // given
        when(productRepository.existsById("P001")).thenReturn(true);
        when(productRepository.deleteById("P001")).thenReturn(1);

        // when
        productService.deleteProduct("P001");

        // then
        verify(productRepository, times(1)).deleteById("P001");
    }

    @Test
    @Order(12)
    @DisplayName("deleteProduct - 실패 (상품 없음)")
    void deleteProduct_notFound() {
        // given
        when(productRepository.existsById("P001")).thenReturn(false);

        // when, then
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct("P001"));
    }

    @Test
    @Order(13)
    @DisplayName("deleteProduct - 실패 (삭제 실패)")
    void deleteProduct_failToDelete() {
        // given
        when(productRepository.existsById("P001")).thenReturn(true);
        when(productRepository.deleteById("P001")).thenReturn(0);

        // when, then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.deleteProduct("P001"));
        assertTrue(exception.getMessage().contains("Failed to delete product"));
    }

    @Test
    @Order(14)
    @DisplayName("getProducts - 페이징 성공")
    void getProducts() {
        // given
        Page<Product> page = new Page<>(List.of(new Product("P001", "상품1", 10000, 10, List.of("C001"))), 1);
        when(productRepository.findAll(1, 10)).thenReturn(page);

        // when
        Page<Product> result = productService.getProducts(1, 10);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalCount());
    }

    @Test
    @Order(15)
    @DisplayName("getProductsByCategory - 카테고리별 페이징 성공")
    void getProductsByCategory() {
        // given
        Page<Product> page = new Page<>(List.of(new Product("P001", "상품1", 10000, 10, List.of("C001"))), 1);
        when(productRepository.findByCategory("C001", 1, 10)).thenReturn(page);

        // when
        Page<Product> result = productService.getProductsByCategory("C001", 1, 10);

        // then
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalCount());
    }
}
