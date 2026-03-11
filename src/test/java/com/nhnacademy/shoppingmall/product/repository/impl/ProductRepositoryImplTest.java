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
import java.util.Optional;

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

        testCategory1 = new Category("cat_test_01", "Test Category 1", 1);
        testCategory2 = new Category("cat_test_02", "Test Category 2", 2);
        categoryRepository.save(testCategory1);
        categoryRepository.save(testCategory2);

        testProduct = new Product(
                "test_prod_01",
                "Test Product",
                10000,
                100,
                Product.NO_IMAGE_PATH,
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
    @DisplayName("product 등록")
    void save() {
        Product newProduct = new Product("test_prod_02", "New Product", 20000, 50, Product.NO_IMAGE_PATH, List.of(testCategory2.getCategoryId()));
        
        int result = productRepository.save(newProduct);
        assertEquals(1, result, "Should return 1 when product is successfully saved.");

        Optional<Product> savedProduct = productRepository.findById("test_prod_02");
        assertTrue(savedProduct.isPresent());
        assertEquals("New Product", savedProduct.get().getProductName());
        assertEquals(20000, savedProduct.get().getPrice());
        assertEquals(50, savedProduct.get().getStock());
        assertTrue(savedProduct.get().getCategoryIds().contains(testCategory2.getCategoryId()));
    }

    @Test
    @Order(2)
    @DisplayName("product 조회 테스트")
    void findById() {
        Optional<Product> foundProduct = productRepository.findById(testProduct.getProductId());
        assertTrue(foundProduct.isPresent(), "Product should exist when finding by existing ID.");
        assertEquals(testProduct.getProductId(), foundProduct.get().getProductId());
        assertTrue(foundProduct.get().getCategoryIds().contains(testCategory1.getCategoryId()));

        Optional<Product> notFoundProduct = productRepository.findById("unknown_prod_id");
        assertTrue(notFoundProduct.isEmpty(), "Should return empty Optional when finding by non-existent ID.");
    }

    @Test
    @Order(3)
    @DisplayName("상품 수정 테스트")
    void update() {
        Product updateParam = new Product(
                testProduct.getProductId(), 
                "Updated Product Name", 
                15000, 
                30,
                Product.NO_IMAGE_PATH,
                List.of(testCategory1.getCategoryId(), testCategory2.getCategoryId())
        );

        int result = productRepository.update(updateParam);
        assertEquals(1, result, "Should return 1 when product is successfully updated.");

        Optional<Product> updatedProduct = productRepository.findById(testProduct.getProductId());
        assertTrue(updatedProduct.isPresent());
        assertEquals("Updated Product Name", updatedProduct.get().getProductName());
        assertEquals(15000, updatedProduct.get().getPrice());
        assertEquals(30, updatedProduct.get().getStock());
        assertEquals(2, updatedProduct.get().getCategoryIds().size());
        assertTrue(updatedProduct.get().getCategoryIds().contains(testCategory2.getCategoryId()));
    }

    @Test
    @Order(4)
    @DisplayName("상품 존재 여부 카운트 테스트")
    void countById() {
        int countExist = productRepository.countById(testProduct.getProductId());
        assertEquals(1, countExist, "Count for existing product should be 1.");

        int countNotExist = productRepository.countById("unknown_id");
        assertEquals(0, countNotExist, "Count for non-existent product should be 0.");
    }

    @Test
    @Order(5)
    @DisplayName("상품 삭제 테스트")
    void deleteById() {
        int result = productRepository.deleteById(testProduct.getProductId());
        assertEquals(1, result, "Should return 1 when product is successfully deleted.");

        Optional<Product> deletedProduct = productRepository.findById(testProduct.getProductId());
        assertTrue(deletedProduct.isEmpty(), "Deleted product should not be found.");
    }

    @Test
    @Order(6)
    @DisplayName("상품 전체 페이징 조회 테스트")
    void findAll() {
        Product newProduct = new Product("test_prod_03", "Paging Test Product", 30000, 10, Product.NO_IMAGE_PATH, List.of(testCategory1.getCategoryId()));
        productRepository.save(newProduct);

        int page = 1;
        int pageSize = 10;
        Page<Product> productPage = productRepository.findAll(page, pageSize);

        assertNotNull(productPage);
        assertTrue(productPage.getContent().size() >= 2, "At least 2 products should be retrieved.");
        assertTrue(productPage.getTotalCount() >= 2, "Total count should be correct.");
    }

    @Test
    @Order(7)
    @DisplayName("특정 카테고리에 속한 상품 페이징 조회 테스트")
    void findByCategory() {
        int page = 1;
        int pageSize = 10;
        Page<Product> productPage = productRepository.findByCategory(testCategory1.getCategoryId(), page, pageSize);

        assertNotNull(productPage);
        assertFalse(productPage.getContent().isEmpty(), "Products linked to the category should be retrieved.");
        
        Product foundProduct = productPage.getContent().get(0);
        assertEquals(testProduct.getProductId(), foundProduct.getProductId());
    }
}