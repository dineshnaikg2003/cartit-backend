package com.cartit.service.helper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cartit.entity.Order;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.OrderRepository;

@Service
public class AdminOrderHelperImpl implements AdminOrderHelper {

    private final OrderRepository orderRepository;

    public AdminOrderHelperImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {

        return orderRepository
                .findByActiveTrueOrderByCreatedAtDesc();
    }

    @Override
    public Order getOrder(Long orderId) {

        return orderRepository
                .findByIdAndActiveTrue(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found."));
    }

    @Override
    public Order save(Order order) {

        return orderRepository.save(order);
    }
}