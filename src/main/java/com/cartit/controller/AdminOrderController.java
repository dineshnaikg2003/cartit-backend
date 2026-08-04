package com.cartit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartit.dto.response.ApiResponse;
import com.cartit.dto.response.OrderResponse;
import com.cartit.service.AdminOrderService;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    public AdminOrderController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {

        List<OrderResponse> response =
                adminOrderService.getAllOrders();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Orders fetched successfully.",
                        response));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
            @PathVariable Long orderId) {

        OrderResponse response =
                adminOrderService.getOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order fetched successfully.",
                        response));
    }

    @PatchMapping("/{orderId}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(
            @PathVariable Long orderId) {

        OrderResponse response =
                adminOrderService.confirmOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order confirmed successfully.",
                        response));
    }

    @PatchMapping("/{orderId}/pack")
    public ResponseEntity<ApiResponse<OrderResponse>> packOrder(
            @PathVariable Long orderId) {

        OrderResponse response =
                adminOrderService.packOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order packed successfully.",
                        response));
    }

    @PatchMapping("/{orderId}/out-for-delivery")
    public ResponseEntity<ApiResponse<OrderResponse>> outForDelivery(
            @PathVariable Long orderId) {

        OrderResponse response =
                adminOrderService.outForDelivery(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order marked as out for delivery.",
                        response));
    }

    @PatchMapping("/{orderId}/deliver")
    public ResponseEntity<ApiResponse<OrderResponse>> deliverOrder(
            @PathVariable Long orderId) {

        OrderResponse response =
                adminOrderService.deliverOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order delivered successfully.",
                        response));
    }

    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long orderId) {

        OrderResponse response =
                adminOrderService.cancelOrder(orderId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Order cancelled successfully.",
                        response));
    }
}