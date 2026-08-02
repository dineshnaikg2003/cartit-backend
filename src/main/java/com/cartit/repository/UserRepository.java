package com.cartit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);
}