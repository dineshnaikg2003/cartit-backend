package com.cartit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cartit.dto.request.UpdateLocationRequest;
import com.cartit.dto.request.UpdateOrderStatusRequest;
import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.OrderResponse;
import com.cartit.enums.OrderStatus;
import com.cartit.service.DeliveryService;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/orders/allocated")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllocatedOrders() {
        List<OrderResponse> response = deliveryService.getAllocatedOrders();
        return ResponseEntity.ok(ApiResponse.success("Allocated orders fetched successfully", response));
    }

    @PatchMapping("/location")
    public ResponseEntity<ApiResponse<Void>> updateLocation(@RequestBody UpdateLocationRequest request) {
        deliveryService.updateLocation(request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(ApiResponse.success("Location updated successfully", null));
    }

    @RequestMapping(value = "/orders/{orderId}/status", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse response = deliveryService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Order status updated to " + request.getStatus(), response));
    }

    @PatchMapping("/orders/{orderId}/accept")
    public ResponseEntity<ApiResponse<OrderResponse>> acceptOrder(@PathVariable Long orderId) {
        OrderResponse response = deliveryService.acceptOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order accepted successfully", response));
    }
}
