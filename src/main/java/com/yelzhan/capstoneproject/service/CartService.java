package com.yelzhan.capstoneproject.service;

import com.yelzhan.capstoneproject.config.SessionCart;
import com.yelzhan.capstoneproject.model.entity.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CartService {
    SessionCart addProduct(Long id, HttpSession session);

    void addProductForUser(Long id, String username);

    boolean inCart(Long id);

    Page<CartItem> fetchAllForUser(String email, Optional<Integer> page, Optional<Integer> size, Optional<String> sort);

    boolean inSessionCart(Long id, HttpSession session);

    void deleteProductFromCartItem(Long id, String email);
}
