package com.cartit.service.helper;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.cartit.enums.OrderStatus;
import com.cartit.repository.BrandRepository;
import com.cartit.repository.CategoryRepository;
import com.cartit.repository.OrderRepository;
import com.cartit.repository.ProductRepository;
import com.cartit.repository.UserRepository;

@Service
public class AdminDashboardHelperImpl implements AdminDashboardHelper {

    private static final int LOW_STOCK_THRESHOLD = 10;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final OrderRepository orderRepository;

    public AdminDashboardHelperImpl(
            UserRepository userRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            OrderRepository orderRepository) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Long getTotalUsers() {
        return userRepository.countByActiveTrue();
    }

    @Override
    public Long getTotalProducts() {
        return productRepository.countByActiveTrue();
    }

    @Override
    public Long getTotalCategories() {
        return categoryRepository.countByActiveTrue();
    }

    @Override
    public Long getTotalBrands() {
        return brandRepository.countByActiveTrue();
    }

    @Override
    public Long getTotalOrders() {
        return orderRepository.countByActiveTrue();
    }

    @Override
    public Long getPendingOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.PENDING);
    }

    @Override
    public Long getConfirmedOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.CONFIRMED);
    }

    @Override
    public Long getPackedOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.PACKED);
    }

    @Override
    public Long getOutForDeliveryOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
    }

    @Override
    public Long getDeliveredOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.DELIVERED);
    }

    @Override
    public Long getCancelledOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.CANCELLED);
    }

    @Override
    public BigDecimal getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }

    @Override
    public Long getLowStockProducts() {
        return productRepository.countByStockLessThanAndActiveTrue(LOW_STOCK_THRESHOLD);
    }
}