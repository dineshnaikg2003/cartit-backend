package com.cartit.controller.admin;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.cartit.service.storage.ImageStorageService;

@RestController
@RequestMapping("/api/admin/brands")
public class AdminBrandImageController {

    private final ImageStorageService imageStorageService;

    public AdminBrandImageController(
            ImageStorageService imageStorageService) {
        this.imageStorageService =
                imageStorageService;
    }

    @PostMapping("/logo")
    public ResponseEntity<Map<String, String>> uploadBrandLogo(
            @RequestParam("file") MultipartFile file) {

        String imageUrl =
                imageStorageService.uploadBrandLogo(file);

        return ResponseEntity.ok(
                Map.of("imageUrl", imageUrl)
        );
    }
}