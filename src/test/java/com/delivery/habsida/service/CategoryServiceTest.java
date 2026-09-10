package com.delivery.habsida.service;

import com.delivery.habsida.dto.CategoryCreateRequest;
import com.delivery.habsida.dto.CategoryDTO;
import com.delivery.habsida.entity.Category;
import com.delivery.habsida.entity.Store;
import com.delivery.habsida.exception.CategoryNotFoundException;
import com.delivery.habsida.repository.CategoryRepository;
import com.delivery.habsida.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private Store store;

    @BeforeEach
    void setUp() {
        category = new Category();
        store = new Store();

        category.setName("test");
        category.setDescription("description");
        category.setCategoryPhoto("categoryPhoto");
        category.setStore(store);
        category.setId(1L);

        store.setId(1L);
    }

    @Test
    void shouldReturnListOfCategories_getCategories() {
        when(categoryRepository.findByStoreId(1L)).thenReturn(List.of(category));
        List<CategoryDTO> categoryDTOList = categoryService.getCategories(1L, null);

        assertEquals(1, categoryDTOList.size());
        assertEquals("test", categoryDTOList.get(0).name());
    }

    // Создание категории
    @Test
    void createCategory_shouldCreateCategory() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        CategoryCreateRequest request = new CategoryCreateRequest("name", "desc", "photo");

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO result = categoryService.createCategory(1L, request);
        assertEquals("test", result.name());
    }

    // Успешное обновление
    @Test
    void updateCategory_shouldUpdateCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        CategoryCreateRequest request = new CategoryCreateRequest("name", "desc", "photo");
        CategoryDTO updatedCategory = categoryService.updateCategory(1L, 1L, request);
        assertEquals("name", updatedCategory.name());
    }

    // Категория не найдена
    @Test
    void updateCategory_shouldThrowException_whenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        CategoryCreateRequest request = new CategoryCreateRequest("name", "desc", "photo");
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(store.getId(), category.getId(), request));
    }

    // Категория найдена, но принадлежит другому магазину
    @Test
    void updateCategory_shouldThrowException_whenCategoryBelongsToDifferentStore() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        CategoryCreateRequest request = new CategoryCreateRequest("name", "desc", "photo");
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(99L, 1L, request));

    }

    // Успешное удаление
    @Test
    void deleteCategory_shouldDeleteCategory() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        categoryService.deleteCategory(1L, 1L);
        verify(categoryRepository).delete(category);

    }

    // Категория не найдена
    @Test
    void deleteCategory_shouldThrowException_whenCategoryNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(1L, 1L));

    }

    // Категория найдена, но принадлежит другому магазину
    @Test
    void deleteCategory_shouldThrowException_whenCategoryBelongsToDifferentStore() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(99L, 1L));

    }
}

