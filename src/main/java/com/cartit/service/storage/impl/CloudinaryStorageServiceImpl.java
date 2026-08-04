package com.cartit.service.storage.impl;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cartit.exception.BadRequestException;
import com.cartit.service.storage.ImageStorageService;

@Service
public class CloudinaryStorageServiceImpl implements ImageStorageService {

    private final Cloudinary cloudinary;

    public CloudinaryStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadProductImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Image file is required.");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new BadRequestException("Only image files are allowed.");
        }

        try {

            String fileName = UUID.randomUUID().toString();

            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "cartit/products",
                            "public_id", fileName,
                            "resource_type", "image"));

            return result.get("secure_url").toString();

        } catch (IOException ex) {

            throw new BadRequestException(
                    "Failed to upload product image.");
        }
    }

    @Override
    public void deleteProductImage(String imageUrl) {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        try {

            String publicId = extractPublicId(imageUrl);

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.emptyMap());

        } catch (Exception ex) {

            throw new BadRequestException(
                    "Failed to delete product image.");
        }
    }

    private String extractPublicId(String imageUrl) {

        String[] parts = imageUrl.split("/");

        String fileName = parts[parts.length - 1];

        fileName = fileName.substring(0, fileName.lastIndexOf('.'));

        return "cartit/products/" + fileName;
    }
}