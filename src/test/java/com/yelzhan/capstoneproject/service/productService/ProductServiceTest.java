package com.yelzhan.capstoneproject.service.productService;

import com.yelzhan.capstoneproject.model.entity.Category;
import com.yelzhan.capstoneproject.model.entity.Product;
import com.yelzhan.capstoneproject.repository.ProductRepository;
import com.yelzhan.capstoneproject.service.CategoryService;
import com.yelzhan.capstoneproject.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTest {


    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testExistsByProductName_Exists() {
        String productName = "Test Product";
        when(productRepository.existsByName(eq(productName.trim()))).thenReturn(true);

        boolean exists = productService.existsByProductName(productName);

        assertTrue(exists);
        verify(productRepository, times(1)).existsByName(eq(productName.trim()));
    }

    @Test
    public void testExistsByProductName_NotExists() {
        String productName = "Non-existent Product";
        when(productRepository.existsByName(eq(productName.trim()))).thenReturn(false);

        boolean exists = productService.existsByProductName(productName);

        assertFalse(exists);
        verify(productRepository, times(1)).existsByName(eq(productName.trim()));
    }

    @Test
    public void testFetchAllByCategory() {
        String categoryName = "Test Category";
        Category category = new Category(categoryName, "Desc 1");
        Pageable pageable = Pageable.ofSize(2);
        Page<Product> expectedPage = new PageImpl<>(List.of(new Product(), new Product()), pageable, 2);

        when(categoryService.fetchByCategoryName(eq(categoryName.trim()))).thenReturn(category);
        when(productRepository.findAllByCategory(eq(category), any(Pageable.class))).thenReturn(expectedPage);

        Page<Product> actualPage = productService.fetchAllByCategory(categoryName, Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals(expectedPage, actualPage);
        verify(categoryService, times(1)).fetchByCategoryName(eq(categoryName.trim()));
        verify(productRepository, times(1)).findAllByCategory(eq(category), any(Pageable.class));
    }

    @Test
    public void testFetchById() {
        Long productId = 1L;
        Product expectedProduct = new Product();

        when(productRepository.findById(eq(productId))).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.fetchById(productId);

        assertEquals(expectedProduct, actualProduct);
        verify(productRepository, times(1)).findById(eq(productId));
    }
}
