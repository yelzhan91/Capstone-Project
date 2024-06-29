package com.yelzhan.capstoneproject.service.implementation;

import com.yelzhan.capstoneproject.config.SessionCart;
import com.yelzhan.capstoneproject.exception.ResourceNotFoundException;
import com.yelzhan.capstoneproject.model.entity.CartItem;
import com.yelzhan.capstoneproject.model.entity.Product;
import com.yelzhan.capstoneproject.model.entity.User;
import com.yelzhan.capstoneproject.repository.CartItemRepository;
import com.yelzhan.capstoneproject.repository.ProductRepository;
import com.yelzhan.capstoneproject.service.CartService;
import com.yelzhan.capstoneproject.service.ProductService;
import com.yelzhan.capstoneproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.yelzhan.capstoneproject.controller.CartController.CART_ATTRIBUTE;
import static com.yelzhan.capstoneproject.service.Page.getPageable;


@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final int DEFAULT_QUANTITY = 0;


    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Override
    public SessionCart addProduct(Long id, HttpSession session) {
        Product product = productService.fetchById(id);
        if(product.getQuantity() <= 0){
            throw new ResourceNotFoundException("Not in stock");
        }
        SessionCart sessionCart = (SessionCart) session.getAttribute(CART_ATTRIBUTE);
        if (sessionCart == null) {
            sessionCart = new SessionCart();
            session.setAttribute(CART_ATTRIBUTE, sessionCart);
        }
        if(!sessionCart.existsByProduct(id)){
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .customer(null)
                    .quantity(DEFAULT_QUANTITY)
                    .build();

            sessionCart.addItem(cartItem);
            product.setQuantity(product.getQuantity() - 1);
            productRepository.save(product);
        }

        sessionCart.setCartItems(sessionCart.getItems().stream()
                .peek(item -> item.getProduct().setQuantity(item.getProduct().getQuantity() + 1))
                .toList());

        product.setQuantity(product.getQuantity() - 1);
        productRepository.save(product);

        return sessionCart;
    }

    @Override
    public void addProductForUser(Long id, String username) {
        Product product = productService.fetchById(id);
        if(product.getQuantity() <= 0){
            throw new ResourceNotFoundException("Not in stock");
        }

        User customer = userService.fetchUserByEmail(username);

        if(!cartItemRepository.existsByProduct(product)){
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .customer(customer)
                    .quantity(DEFAULT_QUANTITY)
                    .build();

            cartItemRepository.save(cartItem);

            product.setQuantity(product.getQuantity() - 1);
            productRepository.save(product);
        }

        var customersCart = cartItemRepository.findCartItemsByCustomerAndProduct(customer, product)
                .orElseThrow(()-> new ResourceNotFoundException("Customer " + username + "doesnt have cart items"));

        customersCart.setQuantity(customersCart.getQuantity() + 1);
        cartItemRepository.save(customersCart);

        product.setQuantity(product.getQuantity() - 1);
        productRepository.save(product);

    }

    @Override
    public boolean inCart(Long id) {
        return cartItemRepository.existsByProductIdAndQuantityGreaterThan(id, 0);
    }

    @Override
    public Page<CartItem> fetchAllForUser(String email,
                                          Optional<Integer> page,
                                          Optional<Integer> size,
                                          Optional<String> sort) {
        User customer = userService.fetchUserByEmail(email);

        return cartItemRepository.findCartItemsByCustomer(customer, getPageable(page, size, sort));
    }

    @Override
    public boolean inSessionCart(Long id, HttpSession session) {
        SessionCart sessionCart = (SessionCart) session.getAttribute(CART_ATTRIBUTE);
        return sessionCart.existsByProduct(id);
    }

    @Override
    public void deleteProductFromCartItem(Long id, String email) {

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("CartItem mot found"));

        int quantity = cartItem.getQuantity();
        Product product = productService.fetchById(cartItem.getProduct().getId());

        product.setQuantity(product.getQuantity() + quantity);
        productRepository.save(product);
        cartItemRepository.deleteById(id);

    }
}
