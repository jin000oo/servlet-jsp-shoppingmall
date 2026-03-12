package com.nhnacademy.shoppingmall.product.service.impl;

import com.nhnacademy.shoppingmall.product.domain.Category;
import com.nhnacademy.shoppingmall.product.exception.CategoryAlreadyExistsException;
import com.nhnacademy.shoppingmall.product.exception.CategoryNotFoundException;
import com.nhnacademy.shoppingmall.product.repository.CategoryRepository;
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
class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Test
    @Order(1)
    @DisplayName("saveCategory - 성공")
    void saveCategory() {
        // given
        Category category = new Category("Test_C001", "카테고리1", 1);
        when(categoryRepository.existsById("Test_C001")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(1);

        // when
        categoryService.saveCategory(category);

        // then
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @Order(2)
    @DisplayName("saveCategory - 실패 (이미 존재하는 카테고리)")
    void saveCategory_alreadyExists() {
        // given
        Category category = new Category("Test_C001", "카테고리1", 1);
        when(categoryRepository.existsById("Test_C001")).thenReturn(true);

        // when, then
        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.saveCategory(category));
    }

    @Test
    @Order(3)
    @DisplayName("saveCategory - 실패 (저장 실패)")
    void saveCategory_failToSave() {
        // given
        Category category = new Category("Test_C001", "카테고리1", 1);
        when(categoryRepository.existsById("Test_C001")).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(0);

        // when, then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.saveCategory(category));
        assertTrue(exception.getMessage().contains("Failed to register category"));
    }

    @Test
    @Order(4)
    @DisplayName("updateCategory - 성공")
    void updateCategory() {
        // given
        Category category = new Category("Test_C001", "카테고리1_수정", 2);
        when(categoryRepository.existsById("Test_C001")).thenReturn(true);
        when(categoryRepository.update(category)).thenReturn(1);

        // when
        categoryService.updateCategory(category);

        // then
        verify(categoryRepository, times(1)).update(category);
    }

    @Test
    @Order(5)
    @DisplayName("updateCategory - 실패 (카테고리 없음)")
    void updateCategory_notFound() {
        // given
        Category category = new Category("Test_C001", "카테고리1_수정", 2);
        when(categoryRepository.existsById("Test_C001")).thenReturn(false);

        // when, then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(category));
    }

    @Test
    @Order(6)
    @DisplayName("updateCategory - 실패 (수정 실패)")
    void updateCategory_failToUpdate() {
        // given
        Category category = new Category("Test_C001", "카테고리1_수정", 2);
        when(categoryRepository.existsById("Test_C001")).thenReturn(true);
        when(categoryRepository.update(category)).thenReturn(0);

        // when, then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.updateCategory(category));
        assertTrue(exception.getMessage().contains("Failed to modify category"));
    }

    @Test
    @Order(7)
    @DisplayName("deleteCategory - 성공")
    void deleteCategory() {
        // given
        when(categoryRepository.existsById("Test_C001")).thenReturn(true);
        when(categoryRepository.deleteById("Test_C001")).thenReturn(1);

        // when
        categoryService.deleteCategory("Test_C001");

        // then
        verify(categoryRepository, times(1)).deleteById("Test_C001");
    }

    @Test
    @Order(8)
    @DisplayName("deleteCategory - 실패 (카테고리 없음)")
    void deleteCategory_notFound() {
        // given
        when(categoryRepository.existsById("Test_C001")).thenReturn(false);

        // when, then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory("Test_C001"));
    }

    @Test
    @Order(9)
    @DisplayName("deleteCategory - 실패 (삭제 실패)")
    void deleteCategory_failToDelete() {
        // given
        when(categoryRepository.existsById("Test_C001")).thenReturn(true);
        when(categoryRepository.deleteById("Test_C001")).thenReturn(0);

        // when, then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.deleteCategory("Test_C001"));
        assertTrue(exception.getMessage().contains("Failed to delete category"));
    }

    @Test
    @Order(10)
    @DisplayName("getCategory - 성공")
    void getCategory() {
        // given
        Category category = new Category("Test_C001", "카테고리1", 1);
        when(categoryRepository.findById("Test_C001")).thenReturn(Optional.of(category));

        // when
        Category result = categoryService.getCategory("Test_C001");

        // then
        assertNotNull(result);
        assertEquals("Test_C001", result.getCategoryId());
        assertEquals("카테고리1", result.getCategoryName());
        assertEquals(1, result.getSortOrder());
        verify(categoryRepository, times(1)).findById("Test_C001");
    }

    @Test
    @Order(11)
    @DisplayName("getCategory - 실패 (카테고리 없음)")
    void getCategory_notFound() {
        // given
        when(categoryRepository.findById("Test_C001")).thenReturn(Optional.empty());

        // when, then
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class, () -> categoryService.getCategory("Test_C001"));
        assertTrue(exception.getMessage().contains("Test_C001"));
    }

    @Test
    @Order(12)
    @DisplayName("getCategories - 성공")
    void getCategories() {
        // given
        List<Category> categories = List.of(
                new Category("Test_C001", "카테고리1", 1),
                new Category("Test_C002", "카테고리2", 2)
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        // when
        List<Category> result = categoryService.getCategories();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test_C001", result.getFirst().getCategoryId());
        verify(categoryRepository, times(1)).findAll();
    }
}