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
	private final com.cartit.service.RoutingService routingService;
	private final com.cartit.service.StoreService storeService;

	public OrderServiceImpl(OrderHelper orderHelper, OrderValidator orderValidator,
			OrderResponseBuilder orderResponseBuilder, InventoryService inventoryService,
			com.cartit.service.RoutingService routingService, com.cartit.service.StoreService storeService) {

		this.orderHelper = orderHelper;
		this.orderValidator = orderValidator;
		this.orderResponseBuilder = orderResponseBuilder;
		this.inventoryService = inventoryService;
		this.routingService = routingService;
		this.storeService = storeService;
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

	@Override
	@Transactional(readOnly = true)
	public com.cartit.dto.response.RouteResponse getOrderRoute(Long orderId, Double originLat, Double originLng) {
		Order order = orderHelper.getOrder(orderId);

		Double destLat = order.getDeliveryLatitude();
		Double destLng = order.getDeliveryLongitude();

		Double startLat = originLat;
		Double startLng = originLng;

		if (startLat == null || startLng == null) {
			if (order.getCurrentDeliveryLatitude() != null && order.getCurrentDeliveryLongitude() != null) {
				startLat = order.getCurrentDeliveryLatitude();
				startLng = order.getCurrentDeliveryLongitude();
			} else {
				com.cartit.entity.Store store = storeService.getStore();
				if (store != null) {
					startLat = store.getLatitude();
					startLng = store.getLongitude();
				}
			}
		}

		if (startLat == null || startLng == null || destLat == null || destLng == null) {
			com.cartit.dto.response.RouteResponse err = new com.cartit.dto.response.RouteResponse();
			err.setStatus("FAILED");
			err.setErrorMessage("Order coordinates incomplete for route calculation");
			return err;
		}

		return routingService.calculateRoute(startLat, startLng, destLat, destLng);
	}
}