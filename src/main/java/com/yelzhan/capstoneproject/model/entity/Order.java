package com.yelzhan.capstoneproject.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop_order")
public class Order {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "order_price", columnDefinition = "MONEY")
    private BigDecimal orderPrice;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @JoinColumn(name = "customer_id")
    @ManyToOne(targetEntity = User.class)
    private User customer;

    @JoinColumn(name = "status_id")
    @ManyToOne(targetEntity = OrderStatus.class)
    private OrderStatus status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderDetail orderDetail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) &&
                Objects.equals(createdAt, order.createdAt) &&
                Objects.equals(orderPrice, order.orderPrice) &&
                Objects.equals(address, order.address) &&
                Objects.equals(customer, order.customer) &&
                Objects.equals(status, order.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdAt, orderPrice, address, customer, status);
    }
}
