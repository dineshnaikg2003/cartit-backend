package com.cartit.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cartit.entity.base.BaseEntity;
import com.cartit.enums.AddressType;
import com.cartit.enums.OrderStatus;
import com.cartit.enums.PaymentMethod;
import com.cartit.enums.PaymentStatus;

import jakarta.persistence.*;

@Entity
@Table(
    name = "orders",
    indexes = {
        @Index(name = "idx_orders_delivery_boy_id", columnList = "delivery_boy_id"),
        @Index(name = "idx_orders_user_id", columnList = "user_id"),
        @Index(name = "idx_orders_status", columnList = "orderStatus")
    }
)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_boy_id")
    private User deliveryBoy;

    private Double currentDeliveryLatitude;
    private Double currentDeliveryLongitude;
    private Double currentDeliveryAccuracy;
    private Double currentDeliverySpeed;
    private Double currentDeliveryHeading;
    private LocalDateTime currentDeliveryUpdatedAt;

    public User getDeliveryBoy() {
        return deliveryBoy;
    }

    public void setDeliveryBoy(User deliveryBoy) {
        this.deliveryBoy = deliveryBoy;
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

    public Double getCurrentDeliveryAccuracy() {
        return currentDeliveryAccuracy;
    }

    public void setCurrentDeliveryAccuracy(Double currentDeliveryAccuracy) {
        this.currentDeliveryAccuracy = currentDeliveryAccuracy;
    }

    public Double getCurrentDeliverySpeed() {
        return currentDeliverySpeed;
    }

    public void setCurrentDeliverySpeed(Double currentDeliverySpeed) {
        this.currentDeliverySpeed = currentDeliverySpeed;
    }

    public Double getCurrentDeliveryHeading() {
        return currentDeliveryHeading;
    }

    public void setCurrentDeliveryHeading(Double currentDeliveryHeading) {
        this.currentDeliveryHeading = currentDeliveryHeading;
    }

    public LocalDateTime getCurrentDeliveryUpdatedAt() {
        return currentDeliveryUpdatedAt;
    }

    public void setCurrentDeliveryUpdatedAt(LocalDateTime currentDeliveryUpdatedAt) {
        this.currentDeliveryUpdatedAt = currentDeliveryUpdatedAt;
    }

    // ==========================
    // Delivery Address Snapshot
    // ==========================

    @Column(nullable = false)
    private String deliveryName;

    @Column(nullable = false, length = 10)
    private String deliveryPhone;

    @Column(length = 10)
    private String deliveryAlternatePhone;

    @Column(nullable = false)
    private String deliveryAddressLine1;

    private String deliveryAddressLine2;

    private String deliveryLandmark;

    @Column(nullable = false)
    private String deliveryCity;

    @Column(nullable = false)
    private String deliveryState;

    @Column(nullable = false)
    private String deliveryCountry;

    @Column(nullable = false)
    private String deliveryPostalCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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

    // ==========================
    // Order Details
    // ==========================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    // ==========================
    // Amount Details
    // ==========================

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal=BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryCharge=BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discount=BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal tax=BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount=BigDecimal.ZERO;

    // ==========================
    // Rating & Feedback
    // ==========================

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "review_comment", length = 1000)
    private String reviewComment;

    @Column(name = "rated_at")
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

    @Column(nullable = false)
    private LocalDateTime placedAt;

    // ==========================
    // Order Items
    // ==========================

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

  
}