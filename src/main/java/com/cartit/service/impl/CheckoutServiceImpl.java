package com.cartit.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.CheckoutRequest;
import com.cartit.dto.response.OrderResponse;
import com.cartit.entity.Address;
import com.cartit.entity.Cart;
import com.cartit.entity.Coupon;
import com.cartit.entity.CouponUsage;
import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;
import com.cartit.entity.User;
import com.cartit.enums.OrderStatus;
import com.cartit.exception.BadRequestException;
import com.cartit.repository.CouponRepository;
import com.cartit.repository.CouponUsageRepository;
import com.cartit.repository.OrderRepository;
import com.cartit.security.CurrentUserService;
import com.cartit.service.CheckoutService;
import com.cartit.service.InventoryService;
import com.cartit.service.builder.OrderFactory;
import com.cartit.service.builder.OrderResponseBuilder;
import com.cartit.service.helper.AddressHelper;
import com.cartit.service.helper.CartHelper;
import com.cartit.service.helper.OrderHelper;
import com.cartit.service.validator.OrderValidator;

@Service
@Transactional
public class CheckoutServiceImpl implements CheckoutService {

	private final CartHelper cartHelper;
	private final AddressHelper addressHelper;
	private final OrderHelper orderHelper;
	private final OrderValidator orderValidator;
	private final OrderFactory orderFactory;
	private final InventoryService inventoryService;
	private final OrderResponseBuilder orderResponseBuilder;
	private final CouponRepository couponRepository;
	private final CouponUsageRepository couponUsageRepository;
	private final CurrentUserService currentUserService;
	private final OrderRepository orderRepository;
	private final com.cartit.service.StoreService storeService;
	private final com.cartit.service.OrderAllocationService orderAllocationService;

	public CheckoutServiceImpl(CartHelper cartHelper, AddressHelper addressHelper, OrderHelper orderHelper,
			OrderValidator orderValidator, OrderFactory orderFactory, InventoryService inventoryService,
			OrderResponseBuilder orderResponseBuilder, CouponRepository couponRepository,
			CouponUsageRepository couponUsageRepository, CurrentUserService currentUserService,
			OrderRepository orderRepository, com.cartit.service.StoreService storeService,
			com.cartit.service.OrderAllocationService orderAllocationService) {

		this.cartHelper = cartHelper;
		this.addressHelper = addressHelper;
		this.orderHelper = orderHelper;
		this.orderValidator = orderValidator;
		this.orderFactory = orderFactory;
		this.inventoryService = inventoryService;
		this.orderResponseBuilder = orderResponseBuilder;
		this.couponRepository = couponRepository;
		this.couponUsageRepository = couponUsageRepository;
		this.currentUserService = currentUserService;
		this.orderRepository = orderRepository;
		this.storeService = storeService;
		this.orderAllocationService = orderAllocationService;
	}

	@Override
	public OrderResponse checkout(CheckoutRequest request) {

		User currentUser = currentUserService.getCurrentUser();
		List<OrderStatus> terminalStatuses = List.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED);
		boolean hasActiveOrder = orderRepository.existsByUserIdAndOrderStatusNotInAndActiveTrue(currentUser.getId(), terminalStatuses);
		if (hasActiveOrder) {
			throw new BadRequestException("You already have an active order in progress. You can place a new order once your current order is delivered or cancelled.");
		}

		Cart cart = cartHelper.getActiveCart();

		Coupon coupon = null;

		if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {

			coupon = validateCheckoutCoupon(request.getCouponCode(), cart.getSubTotal());
		}

		Address address = addressHelper.getAddress(request.getAddressId());

		// Validate delivery radius from store location
		if (address.getLatitude() != null && address.getLongitude() != null) {
			com.cartit.entity.Store store = storeService.getStore();
			double distanceKm = storeService.calculateDistanceKm(
					store.getLatitude(), store.getLongitude(),
					address.getLatitude(), address.getLongitude());
			if (distanceKm > store.getMaxDeliveryRadiusKm()) {
				throw new BadRequestException(String.format(
						"Delivery address is %.1f km away, which exceeds our maximum service radius of %.1f km from our store (%s).",
						distanceKm, store.getMaxDeliveryRadiusKm(), store.getName()));
			}
		}

		orderValidator.validateCheckout(cart, address);

		Order order = orderFactory.createOrder(cart, address, request, coupon);

		List<OrderItem> orderItems = orderFactory.createOrderItems(order, cart);

		order.setOrderItems(orderItems);

		orderAllocationService.allocateNearestDeliveryBoy(order);

		Order savedOrder = orderHelper.save(order);

		if (coupon != null) {
			recordCouponUsage(coupon, savedOrder);
		}

		inventoryService.reduceStock(cart);

		cartHelper.clearCart(cart);

		return orderResponseBuilder.build(savedOrder);
	}

	private Coupon validateCheckoutCoupon(String code, java.math.BigDecimal subtotal) {

		String normalizedCode = code.trim().toUpperCase();

		Coupon coupon = couponRepository.findByCodeIgnoreCase(normalizedCode)
				.orElseThrow(() -> new BadRequestException("Invalid coupon code."));

		LocalDateTime now = LocalDateTime.now();

		if (!coupon.isActive()) {
			throw new BadRequestException("This coupon is inactive.");
		}

		if (now.isBefore(coupon.getStartDate())) {
			throw new BadRequestException("This coupon is not active yet.");
		}

		if (now.isAfter(coupon.getExpiryDate())) {
			throw new BadRequestException("This coupon has expired.");
		}

		if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {

			throw new BadRequestException("This coupon has reached its usage limit.");
		}

		if (coupon.getMinimumOrderAmount() != null && subtotal.compareTo(coupon.getMinimumOrderAmount()) < 0) {

			throw new BadRequestException("Minimum order amount is ₹" + coupon.getMinimumOrderAmount());
		}

		User user = currentUserService.getCurrentUser();

		if (coupon.isOneUsagePerUser()
				&& couponUsageRepository.existsByCouponIdAndUserId(coupon.getId(), user.getId())) {

			throw new BadRequestException("You have already used this coupon.");
		}

		return coupon;
	}

	private void recordCouponUsage(Coupon coupon, Order order) {

		User user = currentUserService.getCurrentUser();

		CouponUsage usage = new CouponUsage();

		usage.setCoupon(coupon);
		usage.setUserId(user.getId());
		usage.setOrderId(order.getId());
		usage.setUsedAt(LocalDateTime.now());

		couponUsageRepository.save(usage);

		coupon.setUsedCount(coupon.getUsedCount() + 1);

		couponRepository.save(coupon);
	}
}