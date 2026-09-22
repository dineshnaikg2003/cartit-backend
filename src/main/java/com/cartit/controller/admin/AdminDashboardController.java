package com.cartit.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartit.dto.response.AdminDashboardResponse;
import com.cartit.dto.response.ApiResponse;
import com.cartit.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(
            AdminDashboardService adminDashboardService) {

        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {

        AdminDashboardResponse response =
                adminDashboardService.getDashboard();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Dashboard fetched successfully.",
                        response));
    }
}