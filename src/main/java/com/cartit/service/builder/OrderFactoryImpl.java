package com.cartit.service.builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cartit.dto.request.CheckoutRequest;
import com.cartit.entity.Address;
import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;
import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;
import com.cartit.enums.OrderStatus;
import com.cartit.enums.PaymentStatus;
import com.cartit.service.util.OrderNumberGenerator;

@Component
public class OrderFactoryImpl implements OrderFactory {

	private final OrderNumberGenerator orderNumberGenerator;
	private final ProductSnapshotBuilder productSnapshotBuilder;
	private final AmountCalculator amountCalculator;

	public OrderFactoryImpl(OrderNumberGenerator orderNumberGenerator, ProductSnapshotBuilder productSnapshotBuilder,
			AmountCalculator amountCalculator) {

		this.orderNumberGenerator = orderNumberGenerator;
		this.productSnapshotBuilder = productSnapshotBuilder;
		this.amountCalculator = amountCalculator;
	}

	@Override
	public Order createOrder(Cart cart, Address address, CheckoutRequest request) {

		Order order = new Order();

		order.setUser(cart.getUser());

		order.setOrderNumber(orderNumberGenerator.generateOrderNumber());

		// Delivery Snapshot

		order.setDeliveryName(address.getFullName());
		order.setDeliveryPhone(address.getPhoneNumber());
		order.setDeliveryAlternatePhone(address.getAlternatePhoneNumber());

		order.setDeliveryAddressLine1(address.getAddressLine1());
		order.setDeliveryAddressLine2(address.getAddressLine2());
		order.setDeliveryLandmark(address.getLandmark());

		order.setDeliveryCity(address.getCity());
		order.setDeliveryState(address.getState());
		order.setDeliveryCountry(address.getCountry());
		order.setDeliveryPostalCode(address.getPostalCode());

		order.setDeliveryAddressType(address.getAddressType());

		// Status

		order.setOrderStatus(OrderStatus.PENDING);

		order.setPaymentStatus(PaymentStatus.PENDING);

		order.setPaymentMethod(request.getPaymentMethod());

		// Amount

		BigDecimal subTotal = amountCalculator.calculateSubTotal(cart);

		BigDecimal deliveryCharge = amountCalculator.calculateDeliveryCharge(cart);

		BigDecimal discount = amountCalculator.calculateDiscount(cart);

		BigDecimal tax = amountCalculator.calculateTax(cart);

		BigDecimal totalAmount = amountCalculator.calculateTotalAmount(subTotal, deliveryCharge, discount, tax);

		order.setSubTotal(subTotal);
		order.setDeliveryCharge(deliveryCharge);
		order.setDiscount(discount);
		order.setTax(tax);
		order.setTotalAmount(totalAmount);

		order.setPlacedAt(LocalDateTime.now());

		return order;
	}

	@Override
	public List<OrderItem> createOrderItems(Order order, Cart cart) {

		return cart.getItems().stream().filter(CartItem::getActive)
				.map(cartItem -> productSnapshotBuilder.build(order, cartItem)).toList();
	}
}