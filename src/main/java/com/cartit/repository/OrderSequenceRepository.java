package com.cartit.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cartit.entity.OrderSequence;

public interface OrderSequenceRepository
        extends JpaRepository<OrderSequence, Long> {

    Optional<OrderSequence> findBySequenceDate(LocalDate sequenceDate);

}