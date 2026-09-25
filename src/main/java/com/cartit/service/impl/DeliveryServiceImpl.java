package com.cartit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.response.OrderResponse;
import com.cartit.entity.Order;
import com.cartit.entity.User;
import com.cartit.enums.OrderStatus;
import com.cartit.exception.BadRequestException;
import com.cartit.exception.ResourceNotFoundException;
import com.cartit.repository.OrderRepository;
import com.cartit.repository.UserRepository;
import com.cartit.security.CurrentUserService;
import com.cartit.service.DeliveryService;
import com.cartit.service.builder.OrderResponseBuilder;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final OrderResponseBuilder orderResponseBuilder;

    public DeliveryServiceImpl(OrderRepository orderRepository,
                               UserRepository userRepository,
                               CurrentUserService currentUserService,
                               OrderResponseBuilder orderResponseBuilder) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.orderResponseBuilder = orderResponseBuilder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllocatedOrders() {
        User currentUser = currentUserService.getCurrentUser();
        List<Order> orders = orderRepository.findByDeliveryBoyIdAndActiveTrueOrderByCreatedAtDesc(currentUser.getId());
        
        if (orders.isEmpty()) {
            // Also include unassigned active orders near store
            List<OrderStatus> terminalStatuses = List.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED);
            orders = orderRepository.findByDeliveryBoyIsNullAndOrderStatusNotInAndActiveTrue(terminalStatuses);
        }

        return orderResponseBuilder.build(orders);
    }

    @Override
    public void updateLocation(com.cartit.dto.request.UpdateLocationRequest request) {
        if (request == null) {
            throw new BadRequestException("Location update request payload cannot be null");
        }

        User currentUser = currentUserService.getCurrentUser();
        if (currentUser == null || (currentUser.getRole() != com.cartit.enums.Role.DELIVERY_BOY && currentUser.getRole() != com.cartit.enums.Role.ADMIN)) {
            throw new com.cartit.exception.UnauthorizedException("DELIVERY_BOY role required to broadcast location updates");
        }

        if (request.getOrderId() == null) {
            throw new BadRequestException("orderId is required for location tracking updates");
        }

        Double lat = request.getLatitude();
        Double lng = request.getLongitude();

        // 1. Basic coordinate sanity checks
        if (lat == null || lng == null) {
            throw new BadRequestException("latitude and longitude coordinates are required");
        }
        if (lat < -90.0 || lat > 90.0 || lng < -180.0 || lng > 180.0 || (Math.abs(lat) < 0.0001 && Math.abs(lng) < 0.0001)) {
            throw new BadRequestException("Invalid latitude/longitude coordinates");
        }

        // 2. Accuracy check (reject poor accuracy points > 200m)
        if (request.getAccuracy() != null && request.getAccuracy() > 200.0) {
            throw new BadRequestException("GPS accuracy fix too low (" + request.getAccuracy() + "m)");
        }

        // 3. Timestamp sanity check (reject timestamps > 5 min in future or > 30 min in past)
        if (request.getTimestamp() != null) {
            long now = System.currentTimeMillis();
            long diff = now - request.getTimestamp();
            if (diff < -300000 || diff > 1800000) {
                throw new BadRequestException("Stale or invalid timestamp");
            }
        }

        // 4. Distance / Jump check (impossible ground vehicle movement > 200 km/h)
        if (currentUser.getLatitude() != null && currentUser.getLongitude() != null) {
            double distanceMeters = calculateHaversineDistance(
                currentUser.getLatitude(), currentUser.getLongitude(), lat, lng
            );
            if (distanceMeters > 5000.0) {
                throw new BadRequestException("Instant location morphing jump detected (" + (int)distanceMeters + "m)");
            }
        }

        // Persist latest position against driver user profile
        currentUser.setLatitude(lat);
        currentUser.setLongitude(lng);
        userRepository.save(currentUser);

        // Fetch EXACT target order
        Order order = orderRepository.findByIdAndActiveTrue(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.getOrderId()));

        // Verify order assignment
        if (order.getDeliveryBoy() == null || !order.getDeliveryBoy().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Order #" + request.getOrderId() + " is not assigned to current delivery partner");
        }

        // Verify order is in active trackable state
        if (order.getOrderStatus() == OrderStatus.DELIVERED || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order #" + request.getOrderId() + " is in terminal state (" + order.getOrderStatus() + ")");
        }

        // Update ONLY this single explicit order
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        order.setCurrentDeliveryLatitude(lat);
        order.setCurrentDeliveryLongitude(lng);
        if (request.getAccuracy() != null) order.setCurrentDeliveryAccuracy(request.getAccuracy());
        if (request.getSpeed() != null) order.setCurrentDeliverySpeed(request.getSpeed());
        if (request.getHeading() != null) order.setCurrentDeliveryHeading(request.getHeading());
        order.setCurrentDeliveryUpdatedAt(now);
        orderRepository.save(order);
    }

    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth radius in meters
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findByIdAndActiveTrue(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setOrderStatus(status);

        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            User deliveryBoy = order.getDeliveryBoy();
            if (deliveryBoy != null) {
                deliveryBoy.setIsAvailable(true);
                userRepository.save(deliveryBoy);
            }
        }

        Order saved = orderRepository.save(order);
        return orderResponseBuilder.build(saved);
    }

    @Override
    public OrderResponse acceptOrder(Long orderId) {
        User currentUser = currentUserService.getCurrentUser();
        Order order = orderRepository.findByIdAndActiveTrue(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getDeliveryBoy() != null && !order.getDeliveryBoy().getId().equals(currentUser.getId())) {
            throw new BadRequestException("Order is already assigned to another delivery partner");
        }

        order.setDeliveryBoy(currentUser);
        if (currentUser.getLatitude() != null && currentUser.getLongitude() != null) {
            order.setCurrentDeliveryLatitude(currentUser.getLatitude());
            order.setCurrentDeliveryLongitude(currentUser.getLongitude());
        }
        currentUser.setIsAvailable(false);
        userRepository.save(currentUser);

        Order saved = orderRepository.save(order);
        return orderResponseBuilder.build(saved);
    }
}
