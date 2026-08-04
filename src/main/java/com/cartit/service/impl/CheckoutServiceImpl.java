package com.cartit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.request.CheckoutRequest;
import com.cartit.dto.response.OrderResponse;
import com.cartit.entity.Address;
import com.cartit.entity.Cart;
import com.cartit.entity.Order;
import com.cartit.entity.OrderItem;
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

    public CheckoutServiceImpl(
            CartHelper cartHelper,
            AddressHelper addressHelper,
            OrderHelper orderHelper,
            OrderValidator orderValidator,
            OrderFactory orderFactory,
            InventoryService inventoryService,
            OrderResponseBuilder orderResponseBuilder) {

        this.cartHelper = cartHelper;
        this.addressHelper = addressHelper;
        this.orderHelper = orderHelper;
        this.orderValidator = orderValidator;
        this.orderFactory = orderFactory;
        this.inventoryService = inventoryService;
        this.orderResponseBuilder = orderResponseBuilder;
    }

    @Override
    public OrderResponse checkout(
            CheckoutRequest request) {

        Cart cart =
                cartHelper.getActiveCart();

        Address address =
                addressHelper.getAddress(
                        request.getAddressId());

        orderValidator.validateCheckout(
                cart,
                address);

        Order order =
                orderFactory.createOrder(
                        cart,
                        address,
                        request);

        List<OrderItem> orderItems =
                orderFactory.createOrderItems(
                        order,
                        cart);

        order.setOrderItems(orderItems);

        Order savedOrder =
                orderHelper.save(order);

        inventoryService.reduceStock(cart);

        cartHelper.clearCart(cart);

        return orderResponseBuilder.build(
                savedOrder);
    }
}