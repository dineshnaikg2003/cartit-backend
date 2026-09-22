package com.cartit.service.validator;

import org.springframework.stereotype.Component;

import com.cartit.entity.Address;
import com.cartit.entity.Cart;
import com.cartit.entity.CartItem;
import com.cartit.entity.Order;
import com.cartit.enums.OrderStatus;
import com.cartit.enums.PaymentMethod;
import com.cartit.exception.BadRequestException;

@Component
public class OrderValidator {

	public void validatePaymentMethod(PaymentMethod paymentMethod) {

		if (paymentMethod == null) {
			throw new BadRequestException("Payment method is required.");
		}
	}

	public void validateCancellation(Order order) {

		if (order.getOrderStatus() == OrderStatus.DELIVERED) {

			throw new BadRequestException("Delivered orders cannot be cancelled.");
		}

		if (order.getOrderStatus() == OrderStatus.CANCELLED) {

			throw new BadRequestException("Order is already cancelled.");
		}
	}

	public void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {

		if (currentStatus == newStatus) {
			throw new BadRequestException("Order is already in " + newStatus + " status.");
		}

		switch (currentStatus) {

		case PENDING:

		    if (newStatus != OrderStatus.CONFIRMED
		            && newStatus != OrderStatus.CANCELLED) {

		        throw new BadRequestException("Invalid status transition.");
		    }

		    break;

		case CONFIRMED:

		    if (newStatus != OrderStatus.PACKED
		            && newStatus != OrderStatus.CANCELLED) {

		        throw new BadRequestException("Invalid status transition.");
		    }

		    break;

		case PACKED:

		    if (newStatus != OrderStatus.ARRIVED_AT_STORE
		            && newStatus != OrderStatus.OUT_FOR_DELIVERY
		            && newStatus != OrderStatus.CANCELLED) {

		        throw new BadRequestException("Invalid status transition.");
		    }

		    break;

		case ARRIVED_AT_STORE:

		    if (newStatus != OrderStatus.OUT_FOR_DELIVERY
		            && newStatus != OrderStatus.CANCELLED) {

		        throw new BadRequestException("Invalid status transition.");
		    }

		    break;

		case OUT_FOR_DELIVERY:

		    if (newStatus != OrderStatus.ARRIVED_AT_CUSTOMER
		            && newStatus != OrderStatus.DELIVERED
		            && newStatus != OrderStatus.CANCELLED) {

		        throw new BadRequestException("Invalid status transition.");
		    }

		    break;

		case ARRIVED_AT_CUSTOMER:

		    if (newStatus != OrderStatus.DELIVERED
		            && newStatus != OrderStatus.CANCELLED) {

		        throw new BadRequestException("Invalid status transition.");
		    }

		    break;
		case DELIVERED:
		case CANCELLED:

			throw new BadRequestException("Order status cannot be changed.");

		default:

			throw new BadRequestException("Invalid order status.");
		}
	}

	public void validateCustomerCancellation(Order order) {

	    if (order.getOrderStatus() == OrderStatus.PACKED
	            || order.getOrderStatus() == OrderStatus.ARRIVED_AT_STORE
	            || order.getOrderStatus() == OrderStatus.OUT_FOR_DELIVERY
	            || order.getOrderStatus() == OrderStatus.ARRIVED_AT_CUSTOMER
	            || order.getOrderStatus() == OrderStatus.DELIVERED) {

	        throw new BadRequestException(
	                "Order cannot be cancelled after it has been packed.");
	    }

	    if (order.getOrderStatus() == OrderStatus.CANCELLED) {

	        throw new BadRequestException(
	                "Order is already cancelled.");
	    }
	}
	
	public void validateCheckout(Cart cart, Address address) {

		if (cart.getItems().stream().noneMatch(CartItem::getActive)) {

			throw new BadRequestException("Your cart is empty.");
		}

		if (!Boolean.TRUE.equals(address.getActive())) {

			throw new BadRequestException("Selected address is inactive.");
		}
	}
}