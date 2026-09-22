package com.cartit.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.cartit.enums.AddressType;
import com.cartit.enums.OrderStatus;
import com.cartit.enums.PaymentMethod;
import com.cartit.enums.PaymentStatus;

public class OrderResponse {

	private Long id;

	private String orderNumber;

	// Delivery Snapshot

	private String deliveryName;

	private String deliveryPhone;

	private String deliveryAlternatePhone;

	private String deliveryAddressLine1;

	private String deliveryAddressLine2;

	private String deliveryLandmark;

	private String deliveryCity;

	private String deliveryState;

	private String deliveryCountry;

	private String deliveryPostalCode;

	private AddressType deliveryAddressType;

	private Double deliveryLatitude;

	private Double deliveryLongitude;

	public Double getDeliveryLatitude() {
		return deliveryLatitude;
	}

	public void setDeliveryLatitude(Double deliveryLatitude) {
		this.deliveryLatitude = deliveryLatitude;
	}

	public Double getDeliveryLongitude() {
		return deliveryLongitude;
	}

	public void setDeliveryLongitude(Double deliveryLongitude) {
		this.deliveryLongitude = deliveryLongitude;
	}

	private Double distanceKm;

	public Double getDistanceKm() {
		return distanceKm;
	}

	public void setDistanceKm(Double distanceKm) {
		this.distanceKm = distanceKm;
	}

	// Delivery Partner Info
	private Long deliveryBoyId;
	private String deliveryBoyName;
	private String deliveryBoyPhone;
	private Double currentDeliveryLatitude;
	private Double currentDeliveryLongitude;

	// Store Info
	private String storeName;
	private String storeAddress;
	private Double storeLatitude;
	private Double storeLongitude;

	public Long getDeliveryBoyId() {
		return deliveryBoyId;
	}

	public void setDeliveryBoyId(Long deliveryBoyId) {
		this.deliveryBoyId = deliveryBoyId;
	}

	public String getDeliveryBoyName() {
		return deliveryBoyName;
	}

	public void setDeliveryBoyName(String deliveryBoyName) {
		this.deliveryBoyName = deliveryBoyName;
	}

	public String getDeliveryBoyPhone() {
		return deliveryBoyPhone;
	}

	public void setDeliveryBoyPhone(String deliveryBoyPhone) {
		this.deliveryBoyPhone = deliveryBoyPhone;
	}

	public Double getCurrentDeliveryLatitude() {
		return currentDeliveryLatitude;
	}

	public void setCurrentDeliveryLatitude(Double currentDeliveryLatitude) {
		this.currentDeliveryLatitude = currentDeliveryLatitude;
	}

	public Double getCurrentDeliveryLongitude() {
		return currentDeliveryLongitude;
	}

	public void setCurrentDeliveryLongitude(Double currentDeliveryLongitude) {
		this.currentDeliveryLongitude = currentDeliveryLongitude;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public Double getStoreLatitude() {
		return storeLatitude;
	}

	public void setStoreLatitude(Double storeLatitude) {
		this.storeLatitude = storeLatitude;
	}

	public Double getStoreLongitude() {
		return storeLongitude;
	}

	public void setStoreLongitude(Double storeLongitude) {
		this.storeLongitude = storeLongitude;
	}

	// Status

	private OrderStatus orderStatus;

	private PaymentStatus paymentStatus;

	private PaymentMethod paymentMethod;

	// Amounts

	private BigDecimal subTotal;

	private BigDecimal deliveryCharge;

	private BigDecimal discount;

	private BigDecimal tax;

	private BigDecimal totalAmount;

	private LocalDateTime placedAt;

	private Integer rating;

	private String reviewComment;

	private LocalDateTime ratedAt;

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getReviewComment() {
		return reviewComment;
	}

	public void setReviewComment(String reviewComment) {
		this.reviewComment = reviewComment;
	}

	public LocalDateTime getRatedAt() {
		return ratedAt;
	}

	public void setRatedAt(LocalDateTime ratedAt) {
		this.ratedAt = ratedAt;
	}

	private List<OrderItemResponse> items;

	private Integer totalItems;

	public Integer getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}

	public OrderResponse() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getDeliveryPhone() {
		return deliveryPhone;
	}

	public void setDeliveryPhone(String deliveryPhone) {
		this.deliveryPhone = deliveryPhone;
	}

	public String getDeliveryAlternatePhone() {
		return deliveryAlternatePhone;
	}

	public void setDeliveryAlternatePhone(String deliveryAlternatePhone) {
		this.deliveryAlternatePhone = deliveryAlternatePhone;
	}

	public String getDeliveryAddressLine1() {
		return deliveryAddressLine1;
	}

	public void setDeliveryAddressLine1(String deliveryAddressLine1) {
		this.deliveryAddressLine1 = deliveryAddressLine1;
	}

	public String getDeliveryAddressLine2() {
		return deliveryAddressLine2;
	}

	public void setDeliveryAddressLine2(String deliveryAddressLine2) {
		this.deliveryAddressLine2 = deliveryAddressLine2;
	}

	public String getDeliveryLandmark() {
		return deliveryLandmark;
	}

	public void setDeliveryLandmark(String deliveryLandmark) {
		this.deliveryLandmark = deliveryLandmark;
	}

	public String getDeliveryCity() {
		return deliveryCity;
	}

	public void setDeliveryCity(String deliveryCity) {
		this.deliveryCity = deliveryCity;
	}

	public String getDeliveryState() {
		return deliveryState;
	}

	public void setDeliveryState(String deliveryState) {
		this.deliveryState = deliveryState;
	}

	public String getDeliveryCountry() {
		return deliveryCountry;
	}

	public void setDeliveryCountry(String deliveryCountry) {
		this.deliveryCountry = deliveryCountry;
	}

	public String getDeliveryPostalCode() {
		return deliveryPostalCode;
	}

	public void setDeliveryPostalCode(String deliveryPostalCode) {
		this.deliveryPostalCode = deliveryPostalCode;
	}

	public AddressType getDeliveryAddressType() {
		return deliveryAddressType;
	}

	public void setDeliveryAddressType(AddressType deliveryAddressType) {
		this.deliveryAddressType = deliveryAddressType;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public BigDecimal getDeliveryCharge() {
		return deliveryCharge;
	}

	public void setDeliveryCharge(BigDecimal deliveryCharge) {
		this.deliveryCharge = deliveryCharge;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getPlacedAt() {
		return placedAt;
	}

	public void setPlacedAt(LocalDateTime placedAt) {
		this.placedAt = placedAt;
	}

	public List<OrderItemResponse> getItems() {
		return items;
	}

	public void setItems(List<OrderItemResponse> items) {
		this.items = items;
	}

}