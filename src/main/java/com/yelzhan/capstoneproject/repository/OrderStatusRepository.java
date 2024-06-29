package com.yelzhan.capstoneproject.repository;

import com.yelzhan.capstoneproject.model.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}
