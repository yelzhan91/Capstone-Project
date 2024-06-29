package com.yelzhan.capstoneproject.service.implementation;

import com.yelzhan.capstoneproject.exception.ResourceAlreadyExistException;
import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import com.yelzhan.capstoneproject.model.dto.request.Validatable;
import com.yelzhan.capstoneproject.model.dto.request.product.ProductRequest;
import com.yelzhan.capstoneproject.model.entity.Category;
import com.yelzhan.capstoneproject.model.entity.Gender;
import com.yelzhan.capstoneproject.model.entity.Product;
import com.yelzhan.capstoneproject.repository.ProductRepository;
import com.yelzhan.capstoneproject.service.CategoryService;
import com.yelzhan.capstoneproject.service.GenderService;
import com.yelzhan.capstoneproject.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.yelzhan.capstoneproject.service.Page.getPageable;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService, Validatable<ProductRequest> {

    private static final String DEFAULT_GENDER = "Male";

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final GenderService genderService;


    @Override
    public Page<Product> fetchAll(Boolean adult,
                                  Optional<String> gender,
                                  Optional<Integer> page,
                                  Optional<Integer> size,
                                  Optional<String> sort) {

        String _gender = gender.orElse(DEFAULT_GENDER);
        Pageable pageable = getPageable(page, Optional.of(2), sort);

        Gender genderEntity = genderService.fetchGender(_gender);

        return productRepository.findAllByGenderAndAdult(genderEntity, adult, pageable);
    }

    @Override
    public void create(ProductRequest productRequest) {
        checkExistenceForCreation(productRequest);

        Category category = categoryService.fetchByCategoryName(productRequest.getCategory().trim());
        Gender gender = genderService.fetchGender(productRequest.getGender().trim());

        Product product = Product.builder()
                .name(productRequest.getProductName().trim())
                .description(productRequest.getDescription().trim())
                .category(category)
                .image(productRequest.getImage())
                .gender(gender)
                .adult(productRequest.isAdult())
                .quantity(productRequest.getQuantity())
                .unitPrice(productRequest.getUnitPrice())
                .build();

        productRepository.save(product);
        log.info("Product was created");
    }

    @Override
    public boolean existsByProductName(String productName) {
        return productRepository.existsByName(productName.trim());
    }

    @Override
    public Page<Product> fetchAllByCategory(String category,
                                            Optional<Integer> page,
                                            Optional<Integer> size,
                                            Optional<String> sort) {

        Category fetchedCategory = categoryService.fetchByCategoryName(category.trim());
        Pageable pageable = getPageable(page, Optional.of(2), sort);

        return productRepository.findAllByCategory(fetchedCategory, pageable);
    }

    @Override
    public Product fetchById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found."));
    }

    @Override
    public void checkExistenceForCreation(ProductRequest request) throws ResourceAlreadyExistException {
        if (productRepository.existsByName(request.getProductName().trim())) {
            throw new ResourceAlreadyExistException("Product already exist.");
        }
    }
}
