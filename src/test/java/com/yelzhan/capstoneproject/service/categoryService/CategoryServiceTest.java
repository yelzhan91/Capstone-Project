package com.yelzhan.capstoneproject.service.categoryService;

import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;
import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import com.yelzhan.capstoneproject.model.dto.request.category.CategoryRequest;
import com.yelzhan.capstoneproject.model.entity.Category;
import com.yelzhan.capstoneproject.repository.CategoryRepository;
import com.yelzhan.capstoneproject.service.implementation.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFetchAll() {
        List<Category> categories = Arrays.asList(
                new Category("Category 1", "Description 1"),
                new Category("Category 2", "Description 2")
        );

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.fetchAll();

        assertEquals(categories, result);

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testFetchAllPage() {
        Pageable pageable = PageRequest.of(0, 10); // Example: 10 items per page, first page
        List<Category> categories = Arrays.asList(
                new Category("Category 1", "Description 1"),
                new Category("Category 2", "Description 2")
        );
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(categoryPage);

        Page<Category> result = categoryService.fetchAllPage(Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(categoryPage, result);

        verify(categoryRepository, times(1)).findAll(ArgumentMatchers.isA(Pageable.class));
    }

    @Test
    void testCreate() {
        CategoryRequest categoryRequest = new CategoryRequest("Category 1", "Description 1");

        when(categoryRepository.existsByCategory(categoryRequest.getCategory())).thenReturn(false);

        assertDoesNotThrow(() -> categoryService.create(categoryRequest));

        verify(categoryRepository, times(1)).existsByCategory(categoryRequest.getCategory());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testExists_CategoryExists() {
        String category = "Category 1";

        when(categoryRepository.existsByCategory(category)).thenReturn(true);

        assertTrue(categoryService.exists(category));

        verify(categoryRepository, times(1)).existsByCategory(category);
    }

    @Test
    void testExists_CategoryDoesNotExist() {
        String category = "Category 1";

        when(categoryRepository.existsByCategory(category)).thenReturn(false);

        assertFalse(categoryService.exists(category));

        verify(categoryRepository, times(1)).existsByCategory(category);
    }

    @Test
    void testDelete() {
        Long id = 1L;
        Category category = new Category("Category 1", "Description 1");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        assertDoesNotThrow(() -> categoryService.delete(id));

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void testDelete_CategoryNotFound() {
        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.delete(id));

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void testExistsById_CategoryExists() {
        Long id = 1L;

        when(categoryRepository.existsById(id)).thenReturn(true);

        assertTrue(categoryService.existsById(id));

        verify(categoryRepository, times(1)).existsById(id);
    }

    @Test
    void testExistsById_CategoryDoesNotExist() {
        Long id = 1L;

        when(categoryRepository.existsById(id)).thenReturn(false);

        assertFalse(categoryService.existsById(id));

        verify(categoryRepository, times(1)).existsById(id);
    }

    @Test
    void testFetchByCategoryName_CategoryFound() {
        String categoryName = "Category 1";
        Category category = new Category(categoryName, "Description 1");

        when(categoryRepository.findByCategory(categoryName)).thenReturn(Optional.of(category));

        Category result = categoryService.fetchByCategoryName(categoryName);

        assertEquals(category, result);

        verify(categoryRepository, times(1)).findByCategory(categoryName);
    }

    @Test
    void testFetchByCategoryName_CategoryNotFound() {
        String categoryName = "Category 1";

        when(categoryRepository.findByCategory(categoryName)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.fetchByCategoryName(categoryName));

        verify(categoryRepository, times(1)).findByCategory(categoryName);
    }

    @Test
    void testCheckExistenceForCreation_CategoryDoesNotExist() {
        CategoryRequest categoryRequest = new CategoryRequest("Category 1", "Description 1");

        when(categoryRepository.existsByCategory(categoryRequest.getCategory())).thenReturn(false);

        assertDoesNotThrow(() -> categoryService.checkExistenceForCreation(categoryRequest));

        verify(categoryRepository, times(1)).existsByCategory(categoryRequest.getCategory());
    }

    @Test
    void testCheckExistenceForCreation_CategoryExists() {
        CategoryRequest categoryRequest = new CategoryRequest("Category 1", "Description 1");

        when(categoryRepository.existsByCategory(categoryRequest.getCategory())).thenReturn(true);

        assertThrows(ResourceAlreadyExistException.class, () -> categoryService.checkExistenceForCreation(categoryRequest));

        verify(categoryRepository, times(1)).existsByCategory(categoryRequest.getCategory());
    }

}
