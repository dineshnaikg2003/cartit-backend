package com.cartit.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

	String uploadProductImage(MultipartFile file);

	void deleteProductImage(String imageUrl);

	String uploadBrandLogo(MultipartFile file);

	void deleteBrandLogo(String imageUrl);

	String uploadCategoryImage(MultipartFile file);

	void deleteCategoryImage(String imageUrl);
}