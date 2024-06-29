package com.yelzhan.capstoneproject.config;


import com.yelzhan.capstoneproject.model.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
@Scope(value = "session")
public class SessionCart {

    private List<CartItem> cartItems = new ArrayList<>();

    public void addItem(CartItem cartItem) {
        cartItems.add(cartItem);
    }

    public void removeItem(Long productId) {
        cartItems.removeIf(item -> Objects.equals(item.getProduct().getId(), productId));
    }

    public boolean existsByProduct(Long id){
        return getItems().stream().anyMatch(item -> item.getProduct().getId().equals(id));
    }

    public List<CartItem> getItems(){
        return cartItems;
    }

    public void clear(){
        cartItems.clear();
    }

}
