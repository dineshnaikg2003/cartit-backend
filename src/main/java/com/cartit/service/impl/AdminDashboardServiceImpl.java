package com.cartit.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cartit.dto.response.AdminDashboardResponse;
import com.cartit.service.AdminDashboardService;
import com.cartit.service.helper.AdminDashboardHelper;

@Service
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final AdminDashboardHelper adminDashboardHelper;

    public AdminDashboardServiceImpl(
            AdminDashboardHelper adminDashboardHelper) {

        this.adminDashboardHelper = adminDashboardHelper;
    }

    @Override
    public AdminDashboardResponse getDashboard() {

        AdminDashboardResponse response =
                new AdminDashboardResponse();

        response.setTotalUsers(
                adminDashboardHelper.getTotalUsers());

        response.setTotalProducts(
                adminDashboardHelper.getTotalProducts());

        response.setTotalCategories(
                adminDashboardHelper.getTotalCategories());

        response.setTotalBrands(
                adminDashboardHelper.getTotalBrands());

        response.setTotalOrders(
                adminDashboardHelper.getTotalOrders());

        response.setPendingOrders(
                adminDashboardHelper.getPendingOrders());

        response.setConfirmedOrders(
                adminDashboardHelper.getConfirmedOrders());

        response.setPackedOrders(
                adminDashboardHelper.getPackedOrders());

        response.setOutForDeliveryOrders(
                adminDashboardHelper.getOutForDeliveryOrders());

        response.setDeliveredOrders(
                adminDashboardHelper.getDeliveredOrders());

        response.setCancelledOrders(
                adminDashboardHelper.getCancelledOrders());

        response.setTotalRevenue(
                adminDashboardHelper.getTotalRevenue());

        response.setLowStockProducts(
                adminDashboardHelper.getLowStockProducts());

        return response;
    }
}