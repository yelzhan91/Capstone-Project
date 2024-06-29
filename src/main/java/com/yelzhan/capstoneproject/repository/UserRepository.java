package com.yelzhan.capstoneproject.repository;

import com.yelzhan.capstoneproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM site_user WHERE email = ?", nativeQuery = true)
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT is_active FROM site_user WHERE id = ?",nativeQuery = true)
    boolean findByIdAndIsActive(Long id);

    boolean existsByEmail(String email);
}
