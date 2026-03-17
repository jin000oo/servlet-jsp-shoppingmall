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

        testCategory1 = new Category("Test_C001", "카테고리1", 1);
        testCategory2 = new Category("Test_C002", "카테고리2", 2);
        
        if (categoryRepository.existsById(testCategory1.getCategoryId())) {
            categoryRepository.deleteById(testCategory1.getCategoryId());
        }
        categoryRepository.save(testCategory1);
        
        if (categoryRepository.existsById(testCategory2.getCategoryId())) {
            categoryRepository.deleteById(testCategory2.getCategoryId());
        }
        categoryRepository.save(testCategory2);

        testProduct = new Product(
                "Test_P001",
                "상품1",
                10000,
                100,
                "/images/thumb_test1.png",
                List.of(testCategory1.getCategoryId()),
                List.of("/images/test1.png", "/images/test2.png")
        );
        if (productRepository.existsById(testProduct.getProductId())) {
            productRepository.deleteById(testProduct.getProductId());
        }
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
                "Test_P002",
                "상품2",
                20000,
                50,
                "/images/thumb_save1.png",
                List.of(testCategory2.getCategoryId()),
                List.of("/images/save1.png", "/images/save2.png")
        );
        if (productRepository.existsById(newProduct.getProductId())) {
            productRepository.deleteById(newProduct.getProductId());
        }
        
        int result = productRepository.save(newProduct);
        assertEquals(1, result, "Should return 1 when product is successfully saved.");

        Product actualProduct = productRepository.findById("Test_P002").orElse(null);

        assertFalse(Objects.isNull(actualProduct));
        assertAll (
                () -> assertEquals("상품2", actualProduct.getProductName()),
                () -> assertEquals(20000, actualProduct.getPrice()),
                () -> assertEquals(50, actualProduct.getStock()),
                () -> assertTrue(actualProduct.getCategoryIds().contains(testCategory2.getCategoryId())),
                () -> assertEquals("/images/thumb_save1.png", actualProduct.getThumbnailImagePath()),
                () -> assertEquals(2, actualProduct.getDetailImagePaths().size()),
                () -> assertTrue(actualProduct.getDetailImagePaths().contains("/images/save1.png")),
                () -> assertTrue(actualProduct.getDetailImagePaths().contains("/images/save2.png"))
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
                () -> assertEquals(testProduct.getProductName(), foundProduct.getProductName()),
                () -> assertEquals(testProduct.getPrice(), foundProduct.getPrice()),
                () -> assertEquals(testProduct.getStock(), foundProduct.getStock()),
                () -> assertTrue(foundProduct.getCategoryIds().contains(testCategory1.getCategoryId())),
                () -> assertEquals(testProduct.getThumbnailImagePath(), foundProduct.getThumbnailImagePath()),
                () -> assertEquals(2, foundProduct.getDetailImagePaths().size()),
                () -> assertEquals(testProduct.getDetailImagePaths(), foundProduct.getDetailImagePaths())
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
    @DisplayName("update - 성공 (상세 이미지 수정 포함)")
    void update() {
        Product updateParam = new Product(
                testProduct.getProductId(), 
                "상품1_수정", 
                15000, 
                30,
                "/images/thumb_update1.png",
                List.of(testCategory1.getCategoryId(), testCategory2.getCategoryId()),
                List.of("/images/update1.png")
        );

        int result = productRepository.update(updateParam);
        assertEquals(1, result, "update failed");

        Product updatedProduct = productRepository.findById(testProduct.getProductId()).orElse(null);
        assertFalse(Objects.isNull(updatedProduct), "product not found");
        
        assertAll(
                () -> assertEquals(updateParam.getProductName(), updatedProduct.getProductName()),
                () -> assertEquals(updateParam.getPrice(), updatedProduct.getPrice()),
                () -> assertEquals(updateParam.getStock(), updatedProduct.getStock()),
                () -> assertTrue(updatedProduct.getCategoryIds().contains(testCategory1.getCategoryId())),
                () -> assertTrue(updatedProduct.getCategoryIds().contains(testCategory2.getCategoryId())),
                () -> assertEquals(updateParam.getThumbnailImagePath(), updatedProduct.getThumbnailImagePath()),
                () -> assertEquals(updateParam.getDetailImagePaths().size(), updatedProduct.getDetailImagePaths().size()),
                () -> assertTrue(updatedProduct.getDetailImagePaths().contains(updateParam.getDetailImagePaths().get(0)))
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
        Product newProduct = new Product("Test_P003", "상품3", 30000, 10, List.of(testCategory1.getCategoryId()));
        if (productRepository.existsById(newProduct.getProductId())) {
            productRepository.deleteById(newProduct.getProductId());
        }
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
                () -> assertEquals(testProduct.getProductId(), productPage.getContent().getFirst().getProductId())
        );
    }
}