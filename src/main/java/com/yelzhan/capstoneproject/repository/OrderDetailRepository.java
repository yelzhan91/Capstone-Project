package com.yelzhan.capstoneproject.repository;

import com.yelzhan.capstoneproject.model.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
