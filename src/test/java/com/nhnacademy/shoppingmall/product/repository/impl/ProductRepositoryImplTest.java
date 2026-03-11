package com.nhnacademy.shoppingmall.product.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ProductRepositoryImplTest {

    ProductRepository productRepository = new ProductRepositoryImpl();
    CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    Product testProduct;
    Category testCategory1;
    Category testCategory2;

    @BeforeEach
    void setUp() throws SQLException {
        DbConnectionThreadLocal.initialize();

        testCategory1 = new Category("C001", "카테고리1", 1);
        testCategory2 = new Category("C002", "카테고리2", 2);
        categoryRepository.save(testCategory1);
        categoryRepository.save(testCategory2);

        testProduct = new Product(
                "P001",
                "상품1",
                10000,
                100,
                List.of(testCategory1.getCategoryId())
        );
        productRepository.save(testProduct);
    }

    @AfterEach
    void tearDown() throws SQLException {
        DbConnectionThreadLocal.setSqlError(true);
        DbConnectionThreadLocal.reset();
    }

    @Test
    @Order(1)
    @DisplayName("save - 성공")
    void save() {
        Product newProduct = new Product(
                "P002",
                "상품2",
                20000,
                50,
                Product.NO_IMAGE_PATH,
                List.of(testCategory2.getCategoryId())
        );
        
        int result = productRepository.save(newProduct);
        assertEquals(1, result, "Should return 1 when product is successfully saved.");

        Product actualProduct = productRepository.findById("P002").orElse(null);

        assertFalse(Objects.isNull(actualProduct));
        assertAll (
                () -> assertEquals("상품2", actualProduct.getProductName()),
                () -> assertEquals(20000, actualProduct.getPrice()),
                () -> assertEquals(50, actualProduct.getStock()),
                () -> assertTrue(actualProduct.getCategoryIds().contains(testCategory2.getCategoryId()))
        );
    }

    @Test
    @Order(2)
    @DisplayName("findById - 성공")
    void findById() {
        Product foundProduct = productRepository.findById(testProduct.getProductId()).orElse(null);
        assertFalse(Objects.isNull(foundProduct), "product not found");
        
        assertAll(
                () -> assertEquals(testProduct.getProductId(), foundProduct.getProductId()),
                () -> assertTrue(foundProduct.getCategoryIds().contains(testCategory1.getCategoryId()))
        );
    }

    @Test
    @Order(3)
    @DisplayName("findById - 실패 (존재하지 않는 상품)")
    void findById_notFound() {
        Product notFoundProduct = productRepository.findById("unknown_prod_id").orElse(null);
        assertTrue(Objects.isNull(notFoundProduct), "product should be null");
    }

    @Test
    @Order(4)
    @DisplayName("update - 성공")
    void update() {
        Product updateParam = new Product(
                testProduct.getProductId(), 
                "상품1_수정", 
                15000, 
                30,
                Product.NO_IMAGE_PATH,
                List.of(testCategory1.getCategoryId(), testCategory2.getCategoryId())
        );

        int result = productRepository.update(updateParam);
        assertEquals(1, result, "update failed");

        Product updatedProduct = productRepository.findById(testProduct.getProductId()).orElse(null);
        assertFalse(Objects.isNull(updatedProduct), "product not found");
        
        assertAll(
                () -> assertEquals("상품1_수정", updatedProduct.getProductName()),
                () -> assertEquals(15000, updatedProduct.getPrice()),
                () -> assertEquals(30, updatedProduct.getStock()),
                () -> assertEquals(2, updatedProduct.getCategoryIds().size()),
                () -> assertTrue(updatedProduct.getCategoryIds().contains(testCategory2.getCategoryId()))
        );
    }

    @Test
    @Order(5)
    @DisplayName("existsById 테스트")
    void existsById() {
        assertAll(
                () -> assertTrue(productRepository.existsById(testProduct.getProductId()), "product should exist"),
                () -> assertFalse(productRepository.existsById("unknown_id"), "product should not exist")
        );
    }

    @Test
    @Order(7)
    @DisplayName("deleteById - 성공")
    void deleteById() {
        int result = productRepository.deleteById(testProduct.getProductId());
        assertEquals(1, result, "delete failed");

        Product deletedProduct = productRepository.findById(testProduct.getProductId()).orElse(null);
        assertTrue(Objects.isNull(deletedProduct), "product should be deleted");
    }

    @Test
    @Order(8)
    @DisplayName("findAll - 성공")
    void findAll() {
        Product newProduct = new Product("P003", "상품3", 30000, 10, List.of(testCategory1.getCategoryId()));
        productRepository.save(newProduct);

        int page = 1;
        int pageSize = 10;
        Page<Product> productPage = productRepository.findAll(page, pageSize);

        assertFalse(Objects.isNull(productPage), "page is null");
        assertAll(
                () -> assertTrue(productPage.getContent().size() >= 2, "size < 2"),
                () -> assertTrue(productPage.getTotalCount() >= 2, "total count < 2")
        );
    }

    @Test
    @Order(9)
    @DisplayName("findByCategory - 성공")
    void findByCategory() {
        int page = 1;
        int pageSize = 10;
        Page<Product> productPage = productRepository.findByCategory(testCategory1.getCategoryId(), page, pageSize);

        assertFalse(Objects.isNull(productPage), "page is null");
        assertAll(
                () -> assertFalse(productPage.getContent().isEmpty(), "content is empty"),
                () -> assertEquals(testProduct.getProductId(), productPage.getContent().get(0).getProductId())
        );
    }
}