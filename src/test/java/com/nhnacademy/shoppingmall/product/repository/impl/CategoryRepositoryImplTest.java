package com.nhnacademy.shoppingmall.product.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class CategoryRepositoryImplTest {

    CategoryRepository categoryRepository = new CategoryRepositoryImpl();
    Category testCategory;

    @BeforeEach
    void setUp() throws SQLException {
        DbConnectionThreadLocal.initialize();

        testCategory = new Category("C001", "카테고리1", 1);
        categoryRepository.save(testCategory);
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
        Category newCategory = new Category("C002", "카테고리2", 2);
        
        int result = categoryRepository.save(newCategory);
        assertEquals(1, result, "save failed");

        Category actualCategory = categoryRepository.findById("C002").orElse(null);

        assertFalse(Objects.isNull(actualCategory), "category not found");
        assertAll(
                () -> assertEquals("카테고리2", actualCategory.getCategoryName()),
                () -> assertEquals(2, actualCategory.getSortOrder())
        );
    }

    @Test
    @Order(2)
    @DisplayName("findById - 성공")
    void findById() {
        Category foundCategory = categoryRepository.findById(testCategory.getCategoryId()).orElse(null);
        assertFalse(Objects.isNull(foundCategory), "category not found");
        
        assertAll(
                () -> assertEquals(testCategory.getCategoryId(), foundCategory.getCategoryId()),
                () -> assertEquals(testCategory.getCategoryName(), foundCategory.getCategoryName())
        );
    }

    @Test
    @Order(3)
    @DisplayName("findById - 실패 (존재하지 않는 카테고리)")
    void findById_notFound() {
        Category notFoundCategory = categoryRepository.findById("unknown_cat_id").orElse(null);
        assertTrue(Objects.isNull(notFoundCategory), "category should be null");
    }

    @Test
    @Order(4)
    @DisplayName("update - 성공")
    void update() {
        Category updateParam = new Category(testCategory.getCategoryId(), "카테고리1_수정", 99);

        int result = categoryRepository.update(updateParam);
        assertEquals(1, result, "update failed");

        Category updatedCategory = categoryRepository.findById(testCategory.getCategoryId()).orElse(null);
        assertFalse(Objects.isNull(updatedCategory), "category not found");
        
        assertAll(
                () -> assertEquals("카테고리1_수정", updatedCategory.getCategoryName()),
                () -> assertEquals(99, updatedCategory.getSortOrder())
        );
    }

    @Test
    @Order(5)
    @DisplayName("existsById 테스트")
    void existsById() {
        assertAll(
                () -> assertTrue(categoryRepository.existsById(testCategory.getCategoryId()), "category should exist"),
                () -> assertFalse(categoryRepository.existsById("unknown_id"), "category should not exist")
        );
    }

    @Test
    @Order(7)
    @DisplayName("findAll - 성공")
    void findAll() {
        Category newCategory = new Category("C003", "카테고리3", 3);
        categoryRepository.save(newCategory);

        List<Category> categories = categoryRepository.findAll();

        assertFalse(Objects.isNull(categories), "list is null");
        assertTrue(categories.size() >= 2, "size < 2");
    }

    @Test
    @Order(8)
    @DisplayName("deleteById - 성공")
    void deleteById() {
        int result = categoryRepository.deleteById(testCategory.getCategoryId());
        assertEquals(1, result, "delete failed");

        Category deletedCategory = categoryRepository.findById(testCategory.getCategoryId()).orElse(null);
        assertTrue(Objects.isNull(deletedCategory), "category should be deleted");
    }
}