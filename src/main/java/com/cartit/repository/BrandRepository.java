package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	Optional<Brand> findByName(String name);

	boolean existsByName(String name);

	List<Brand> findByActiveTrueOrderByNameAsc();
}
