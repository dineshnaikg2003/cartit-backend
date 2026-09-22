package com.cartit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cartit.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
