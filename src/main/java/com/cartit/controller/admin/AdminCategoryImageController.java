package com.cartit.controller.admin;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cartit.service.storage.ImageStorageService;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryImageController {

    private final ImageStorageService imageStorageService;

    public AdminCategoryImageController(
            ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadCategoryImage(
            @RequestParam("file") MultipartFile file) {

        String imageUrl =
                imageStorageService.uploadCategoryImage(file);

        return ResponseEntity.ok(
                Map.of("imageUrl", imageUrl)
        );
    }
}