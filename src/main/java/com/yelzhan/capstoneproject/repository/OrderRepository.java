package com.yelzhan.capstoneproject.repository;

import com.yelzhan.capstoneproject.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
