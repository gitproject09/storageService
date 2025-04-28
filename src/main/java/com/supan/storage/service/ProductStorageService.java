package com.supan.storage.service;

import com.supan.storage.constants.StoragePath;
import com.supan.storage.model.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for handling product-related file operations
 */
@Service
public class ProductStorageService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageTokenService tokenService;

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Store a product image
     */
    public FileMetadata storeProductImage(MultipartFile file, Long productId) {
        String filename = file.getOriginalFilename();
        String path = StoragePath.getProductImagePath(productId, filename);
        return storageService.storeFile(file, path);
    }

    /**
     * Store a product thumbnail
     */
    public FileMetadata storeProductThumbnail(MultipartFile file, Long productId) {
        String path = StoragePath.getProductThumbnailPath(productId);
        return storageService.storeFile(file, path);
    }

    /**
     * Get a secure URL for a product image
     */
    public String getSecureProductImageUrl(Long productId, String filename) {
        String path = StoragePath.getProductImagePath(productId, filename);
        String token = tokenService.generateToken(path);
        return baseUrl + "/api/files/products/" + productId + "/images/" + filename + "?token=" + token;
    }

    /**
     * Get a secure URL for a product thumbnail
     */
    public String getSecureProductThumbnailUrl(Long productId) {
        String path = StoragePath.getProductThumbnailPath(productId);
        String token = tokenService.generateToken(path);
        return baseUrl + "/api/files/products/" + productId + "/thumbnail?token=" + token;
    }

    /**
     * Delete all files related to a product
     */
    public boolean deleteAllProductFiles(Long productId) {
        // This is a simplified implementation
        // In a real application, you might need to list and delete multiple files
        String thumbnailPath = StoragePath.getProductThumbnailPath(productId);
        storageService.deleteFile(thumbnailPath);

        // You would typically list all files in the product directory and delete them
        // This would require additional MinIO operations
        return true;
    }
}
