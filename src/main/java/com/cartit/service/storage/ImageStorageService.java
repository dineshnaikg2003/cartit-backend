package com.cartit.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String uploadProductImage(MultipartFile file);

    void deleteProductImage(String imageUrl);

}