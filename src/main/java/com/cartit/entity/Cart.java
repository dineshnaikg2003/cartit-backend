package com.cartit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cartit.entity.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "carts")
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @jakarta.persistence.ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimed_offer_id")
    private Offer claimedOffer;

    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CartItem> items = new ArrayList<>();

    public Cart() {
    }
    
    public Integer getTotalItems() {

        return items.stream()
                .filter(CartItem::getActive)
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    
    public BigDecimal getSubTotal() {

        return items.stream()
                .filter(CartItem::getActive)
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Offer getClaimedOffer() {
        return claimedOffer;
    }

    public void setClaimedOffer(Offer claimedOffer) {
        this.claimedOffer = claimedOffer;
    }
}