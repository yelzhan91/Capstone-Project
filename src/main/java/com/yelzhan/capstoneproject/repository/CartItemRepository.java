package com.yelzhan.capstoneproject.repository;

import com.yelzhan.capstoneproject.model.entity.CartItem;
import com.yelzhan.capstoneproject.model.entity.Product;
import com.yelzhan.capstoneproject.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    boolean existsByProductIdAndQuantityGreaterThan(Long id, int quantity);
    Page<CartItem> findCartItemsByCustomer(User customer, Pageable pageable);
    List<CartItem> findCartItemsByCustomer(User customer);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CartItem c WHERE c.product = :product")
    boolean existsByProduct(@Param("product") Product product);

    Optional<CartItem> findCartItemsByCustomerAndProduct(User customer, Product product);

}
