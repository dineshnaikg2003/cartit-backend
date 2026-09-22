package com.cartit.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cartit.entity.Order;
import com.cartit.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByIdAndUserIdAndActiveTrue(Long id, Long userId);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByUserIdAndActiveTrueOrderByCreatedAtDesc(Long userId);

    boolean existsByUserIdAndOrderStatusNotInAndActiveTrue(Long userId, List<OrderStatus> statuses);

    // ===== Admin =====

    Optional<Order> findByIdAndActiveTrue(Long id);

    List<Order> findByActiveTrueOrderByCreatedAtDesc();
    
    long countByActiveTrue();

    long countByOrderStatus(OrderStatus status);

    List<Order> findTop10ByOrderByCreatedAtDesc();
    
    @Query("""
    	    SELECT COALESCE(SUM(o.totalAmount), 0)
    	    FROM Order o
    	    WHERE o.orderStatus = com.cartit.enums.OrderStatus.DELIVERED
    	    """)
    	BigDecimal getTotalRevenue();

    List<Order> findByDeliveryBoyIdAndActiveTrueOrderByCreatedAtDesc(Long deliveryBoyId);

    List<Order> findByDeliveryBoyIdAndOrderStatusNotInAndActiveTrue(Long deliveryBoyId, List<OrderStatus> statuses);

    List<Order> findByDeliveryBoyIsNullAndOrderStatusNotInAndActiveTrue(List<OrderStatus> statuses);
}