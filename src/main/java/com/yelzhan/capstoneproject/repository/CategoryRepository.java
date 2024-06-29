package com.yelzhan.capstoneproject.repository;

import com.yelzhan.capstoneproject.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCategory(String categoryName);
    boolean existsById(Long id);

    Optional<Category> findByCategory(String categoryName);
}
