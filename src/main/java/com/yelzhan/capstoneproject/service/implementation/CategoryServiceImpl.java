package com.yelzhan.capstoneproject.service.implementation;

import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;
import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import com.yelzhan.capstoneproject.model.dto.request.Validatable;
import com.yelzhan.capstoneproject.model.dto.request.category.CategoryRequest;
import com.yelzhan.capstoneproject.model.entity.Category;
import com.yelzhan.capstoneproject.repository.CategoryRepository;
import com.yelzhan.capstoneproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.yelzhan.capstoneproject.service.Page.getPageable;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService, Validatable<CategoryRequest> {

    private final CategoryRepository categoryRepository;


    @Override
    public List<Category> fetchAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> fetchAllPage(Optional<Integer> page, Optional<Integer> size, Optional<String> sort) {
        Pageable pageable = getPageable(page, size, sort);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public void create(CategoryRequest categoryRequest) {

        checkExistenceForCreation(categoryRequest);

        Category category = Category.builder()
                .category(categoryRequest.getCategory())
                .description(categoryRequest.getDescription())
                .build();

        categoryRepository.save(category);
    }

    @Override
    public boolean exists(String category) {
        return categoryRepository.existsByCategory(category);
    }

    @Override
    public void delete(Long id) {
        var resourceFromDb = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(resourceFromDb);
    }

    @Override
    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    @Override
    public Category fetchByCategoryName(String categoryName) {
        return categoryRepository.findByCategory(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public void checkExistenceForCreation(CategoryRequest request) throws ResourceAlreadyExistException {
        if (categoryRepository.existsByCategory(request.getCategory())) {
            throw new ResourceAlreadyExistException("Category already exist");
        }
    }
}
