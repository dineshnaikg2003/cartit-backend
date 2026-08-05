package com.cartit.service.helper;

import java.math.BigDecimal;

public interface AdminDashboardHelper {

    Long getTotalUsers();

    Long getTotalProducts();

    Long getTotalCategories();

    Long getTotalBrands();

    Long getTotalOrders();

    Long getPendingOrders();

    Long getConfirmedOrders();

    Long getPackedOrders();

    Long getOutForDeliveryOrders();

    Long getDeliveredOrders();

    Long getCancelledOrders();

    BigDecimal getTotalRevenue();

    Long getLowStockProducts();
}