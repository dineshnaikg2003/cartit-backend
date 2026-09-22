package com.cartit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.response.OrderResponse;
import com.cartit.entity.Order;
import com.cartit.enums.OrderStatus;
import com.cartit.service.InventoryService;
import com.cartit.service.OrderService;
import com.cartit.service.builder.OrderResponseBuilder;
import com.cartit.service.helper.OrderHelper;
import com.cartit.service.validator.OrderValidator;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderHelper orderHelper;
	private final OrderValidator orderValidator;
	private final OrderResponseBuilder orderResponseBuilder;
	private final InventoryService inventoryService;

	public OrderServiceImpl(OrderHelper orderHelper, OrderValidator orderValidator,
			OrderResponseBuilder orderResponseBuilder, InventoryService inventoryService) {

		this.orderHelper = orderHelper;
		this.orderValidator = orderValidator;
		this.orderResponseBuilder = orderResponseBuilder;
		this.inventoryService = inventoryService;
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getMyOrders() {

		return orderResponseBuilder.build(orderHelper.getMyOrders());
	}

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrder(Long orderId) {

		return orderResponseBuilder.build(orderHelper.getOrder(orderId));
	}

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrderByOrderNumber(String orderNumber) {

		return orderResponseBuilder.build(orderHelper.getOrder(orderNumber));
	}

	private OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {

		Order order = orderHelper.getOrder(orderId);

		orderValidator.validateStatusTransition(order.getOrderStatus(), newStatus);

		if (newStatus == OrderStatus.CANCELLED) {

			orderValidator.validateCustomerCancellation(order);

			inventoryService.restoreStock(order);
		}

		order.setOrderStatus(newStatus);

		Order updatedOrder = orderHelper.save(order);

		return orderResponseBuilder.build(updatedOrder);
	}

	@Override
	public OrderResponse cancelOrder(Long orderId) {

	    return updateOrderStatus(
	            orderId,
	            OrderStatus.CANCELLED);
	}

	@Override
	public OrderResponse rateOrder(Long orderId, Integer rating, String reviewComment) {
		Order order = orderHelper.getOrder(orderId);

		if (rating != null && (rating < 1 || rating > 5)) {
			throw new IllegalArgumentException("Rating must be between 1 and 5 stars");
		}

		order.setRating(rating);
		order.setReviewComment(reviewComment);
		order.setRatedAt(java.time.LocalDateTime.now());

		Order updatedOrder = orderHelper.save(order);
		return orderResponseBuilder.build(updatedOrder);
	}
}