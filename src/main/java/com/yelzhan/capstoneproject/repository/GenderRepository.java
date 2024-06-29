package com.yelzhan.capstoneproject.repository;

import com.yelzhan.capstoneproject.model.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenderRepository extends JpaRepository<Gender, Long> {

    Optional<Gender> findByGender(String gender);
}
