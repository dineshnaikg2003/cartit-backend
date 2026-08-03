package com.cartit.service.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.entity.OrderSequence;
import com.cartit.repository.OrderSequenceRepository;

@Component
public class OrderNumberGeneratorImpl
        implements OrderNumberGenerator {

    private final OrderSequenceRepository repository;

    public OrderNumberGeneratorImpl(
            OrderSequenceRepository repository) {

        this.repository = repository;
    }

    @Override
    @Transactional
    public synchronized String generateOrderNumber() {

        LocalDate today = LocalDate.now();

        OrderSequence sequence = repository
                .findBySequenceDate(today)
                .orElseGet(() -> {

                    OrderSequence newSequence =
                            new OrderSequence();

                    newSequence.setSequenceDate(today);
                    newSequence.setLastSequence(0);

                    return repository.save(newSequence);
                });

        sequence.setLastSequence(
                sequence.getLastSequence() + 1);

        repository.save(sequence);

        String date =
                today.format(
                        DateTimeFormatter.ofPattern("yyMMdd"));

        String runningNumber =
                String.format("%06d",
                        sequence.getLastSequence());

        return "CRT" + date + runningNumber;
    }
}