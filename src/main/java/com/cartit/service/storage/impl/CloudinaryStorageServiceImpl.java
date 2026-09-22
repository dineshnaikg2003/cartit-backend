package com.cartit.service.storage.impl;

import java.io.IOException;
import java.net.URI;
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

	private static final String PRODUCT_FOLDER = "cartit/products";

	private static final String BRAND_FOLDER = "cartit/brands";

	private static final String CATEGORY_FOLDER = "cartit/categories";

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

			String publicId = UUID.randomUUID().toString();

			@SuppressWarnings("unchecked")
			Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(),
					ObjectUtils.asMap("folder", PRODUCT_FOLDER, "public_id", publicId, "resource_type", "image"));

			Object secureUrl = result.get("secure_url");

			if (secureUrl == null) {
				throw new BadRequestException("Failed to generate image URL.");
			}

			return secureUrl.toString();

		} catch (IOException ex) {

			throw new BadRequestException("Failed to upload product image.");
		}
	}

	@Override
	public void deleteProductImage(String imageUrl) {

		if (imageUrl == null || imageUrl.isBlank()) {
			return;
		}

		/*
		 * Only delete images that belong to our Cloudinary product folder.
		 *
		 * This prevents us from trying to delete external images such as Unsplash
		 * images.
		 */
		if (!isCloudinaryProductImage(imageUrl)) {
			return;
		}

		try {

			String publicId = extractPublicId(imageUrl);

			if (publicId == null || publicId.isBlank()) {
				return;
			}

			cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));

		} catch (Exception ex) {

			throw new BadRequestException("Failed to delete product image.");
		}
	}

	private boolean isCloudinaryProductImage(String imageUrl) {

		try {

			URI uri = URI.create(imageUrl);

			String host = uri.getHost();

			if (host == null) {
				return false;
			}

			/*
			 * Only process Cloudinary URLs.
			 */
			if (!host.contains("cloudinary.com")) {
				return false;
			}

			String path = uri.getPath();

			return path != null && path.contains("/" + PRODUCT_FOLDER + "/");

		} catch (Exception ex) {

			return false;
		}
	}

	private String extractPublicId(String imageUrl) {

		URI uri = URI.create(imageUrl);

		String path = uri.getPath();

		if (path == null || path.isBlank()) {
			return null;
		}

		String marker = "/" + PRODUCT_FOLDER + "/";

		int folderIndex = path.indexOf(marker);

		if (folderIndex == -1) {
			return null;
		}

		String publicId = path.substring(folderIndex + 1 + PRODUCT_FOLDER.length() + 1);

		/*
		 * Remove Cloudinary version if present.
		 *
		 * Example:
		 *
		 * /cartit/products/ v1786183213/ abc123.jpg
		 */
		if (publicId.startsWith("v")) {

			int slashIndex = publicId.indexOf('/');

			if (slashIndex > 1) {

				String possibleVersion = publicId.substring(1, slashIndex);

				if (possibleVersion.matches("\\d+")) {

					publicId = publicId.substring(slashIndex + 1);
				}
			}
		}

		/*
		 * Remove file extension.
		 *
		 * abc123.jpg ↓ abc123
		 */
		int extensionIndex = publicId.lastIndexOf('.');

		if (extensionIndex > 0) {

			publicId = publicId.substring(0, extensionIndex);
		}

		return PRODUCT_FOLDER + "/" + publicId;
	}

	private String uploadImage(MultipartFile file, String folder, String imageType) {

		if (file == null || file.isEmpty()) {
			throw new BadRequestException("Image file is required.");
		}

		String contentType = file.getContentType();

		if (contentType == null || !contentType.startsWith("image/")) {

			throw new BadRequestException("Only image files are allowed.");
		}

		try {
			String publicId = UUID.randomUUID().toString();

			@SuppressWarnings("unchecked")
			Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(),
					ObjectUtils.asMap("folder", folder, "public_id", publicId, "resource_type", "image"));

			Object secureUrl = result.get("secure_url");

			if (secureUrl == null) {
				throw new BadRequestException("Failed to generate image URL.");
			}

			return secureUrl.toString();

		} catch (IOException ex) {
			throw new BadRequestException("Failed to upload " + imageType + ".");
		}
	}

	@Override
	public String uploadBrandLogo(MultipartFile file) {

		return uploadImage(file, BRAND_FOLDER, "brand logo");
	}

	@Override
	public String uploadCategoryImage(MultipartFile file) {

		return uploadImage(file, CATEGORY_FOLDER, "category image");
	}

	private void deleteImage(String imageUrl, String folder) {

		if (imageUrl == null || imageUrl.isBlank()) {
			return;
		}

		if (!isCloudinaryImage(imageUrl, folder)) {
			return;
		}

		try {
			String publicId = extractPublicId(imageUrl, folder);

			if (publicId == null || publicId.isBlank()) {
				return;
			}

			cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));

		} catch (Exception ex) {
			throw new BadRequestException("Failed to delete image.");
		}
	}

	private boolean isCloudinaryImage(String imageUrl, String folder) {

		try {
			URI uri = URI.create(imageUrl);

			String host = uri.getHost();

			if (host == null || !host.contains("cloudinary.com")) {
				return false;
			}

			String path = uri.getPath();

			return path != null && path.contains("/" + folder + "/");

		} catch (Exception ex) {
			return false;
		}
	}

	private String extractPublicId(String imageUrl, String folder) {

		URI uri = URI.create(imageUrl);

		String path = uri.getPath();

		if (path == null || path.isBlank()) {
			return null;
		}

		String marker = "/" + folder + "/";

		int folderIndex = path.indexOf(marker);

		if (folderIndex == -1) {
			return null;
		}

		String publicId = path.substring(folderIndex + marker.length());

		if (publicId.startsWith("v")) {

			int slashIndex = publicId.indexOf('/');

			if (slashIndex > 1) {

				String possibleVersion = publicId.substring(1, slashIndex);

				if (possibleVersion.matches("\\d+")) {

					publicId = publicId.substring(slashIndex + 1);
				}
			}
		}

		int extensionIndex = publicId.lastIndexOf('.');

		if (extensionIndex > 0) {
			publicId = publicId.substring(0, extensionIndex);
		}

		return folder + "/" + publicId;
	}

	@Override
	public void deleteBrandLogo(String imageUrl) {

		deleteImage(imageUrl, BRAND_FOLDER);
	}

	@Override
	public void deleteCategoryImage(String imageUrl) {

		deleteImage(imageUrl, CATEGORY_FOLDER);
	}

}