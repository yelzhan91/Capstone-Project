package com.yelzhan.capstoneproject.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item")
public class CartItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private int quantity;

    @JoinColumn(name = "user_id")
    @ManyToOne(targetEntity = User.class)
    private User customer;

    @JoinColumn(name = "product_id")
    @ManyToOne(targetEntity = Product.class)
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem cartItem)) return false;
        return Objects.equals(id, cartItem.id) &&
                Objects.equals(customer, cartItem.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer);
    }
}
