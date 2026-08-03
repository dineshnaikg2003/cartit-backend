package com.cartit.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "order_sequences")
public class OrderSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate sequenceDate;

    @Column(nullable = false)
    private Integer lastSequence;

    public OrderSequence() {
    }

    public Long getId() {
        return id;
    }

    public LocalDate getSequenceDate() {
        return sequenceDate;
    }

    public void setSequenceDate(LocalDate sequenceDate) {
        this.sequenceDate = sequenceDate;
    }

    public Integer getLastSequence() {
        return lastSequence;
    }

    public void setLastSequence(Integer lastSequence) {
        this.lastSequence = lastSequence;
    }
}