package com.yelzhan.capstoneproject.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "quantity_in_stock")
    private Integer quantity;

    @Column(name = "is_adult")
    private boolean adult;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @JoinColumn(name = "category_id")
    @ManyToOne(targetEntity = Category.class)
    private Category category;

    @JoinColumn(name = "gender_id")
    @ManyToOne(targetEntity = Gender.class)
    private Gender gender;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return adult == product.adult &&
                Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(description, product.description) &&
                Objects.equals(image, product.image) &&
                Objects.equals(category, product.category) &&
                Objects.equals(gender, product.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, image, adult, category, gender);
    }
}
