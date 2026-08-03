package com.cartit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserIdAndActiveTrueOrderByDefaultAddressDescCreatedAtDesc(
            Long userId);

    Optional<Address> findByIdAndUserIdAndActiveTrue(
            Long addressId,
            Long userId);

    Optional<Address> findByUserIdAndDefaultAddressTrueAndActiveTrue(
            Long userId);

    boolean existsByUserIdAndActiveTrue(Long userId);
    
    Optional<Address> findFirstByUserIdAndActiveTrueOrderByCreatedAtDesc(
            Long userId);
    
}