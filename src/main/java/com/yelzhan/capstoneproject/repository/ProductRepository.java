package com.yelzhan.capstoneproject.repository;

import com.yelzhan.capstoneproject.model.entity.Category;
import com.yelzhan.capstoneproject.model.entity.Gender;
import com.yelzhan.capstoneproject.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String productName);

    Page<Product> findAllByGenderAndAdult(Gender gender, boolean adult,
                                          Pageable pageable);

    Page<Product> findAllByCategory(Category category,
                                    Pageable pageable);

}
