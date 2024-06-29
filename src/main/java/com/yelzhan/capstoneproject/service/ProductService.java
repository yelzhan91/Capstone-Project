package com.yelzhan.capstoneproject.service;

import com.yelzhan.capstoneproject.model.dto.request.product.ProductRequest;
import com.yelzhan.capstoneproject.model.entity.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductService  {
    Page<Product> fetchAll(Boolean adult,
                           Optional<String> gender,
                           Optional<Integer> page,
                           Optional<Integer> size,
                           Optional<String> sort);

    void create(ProductRequest productRequest);

    boolean existsByProductName(String productName);

    Page<Product> fetchAllByCategory(String category,
                                     Optional<Integer> page,
                                     Optional<Integer> size,
                                     Optional<String> sort);

    Product fetchById(Long id);
}
