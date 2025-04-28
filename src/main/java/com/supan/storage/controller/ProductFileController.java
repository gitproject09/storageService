package com.supan.storage.controller;

import com.supan.storage.model.FileMetadata;
import com.supan.storage.service.ProductStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for product-specific file operations
 */
@RestController
@RequestMapping("/api/products/{productId}/files")
public class ProductFileController {

    @Autowired
    private ProductStorageService productStorageService;

    /**
     * Upload a product image
     */
    @PostMapping("/images")
    public ResponseEntity<FileMetadata> uploadProductImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {

        FileMetadata metadata = productStorageService.storeProductImage(file, productId);
        String secureUrl = productStorageService.getSecureProductImageUrl(productId, metadata.getFilename());
        metadata.setSecureUrl(secureUrl);

        return ResponseEntity.ok(metadata);
    }

    /**
     * Upload a product thumbnail
     */
    @PostMapping("/thumbnail")
    public ResponseEntity<FileMetadata> uploadProductThumbnail(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {

        FileMetadata metadata = productStorageService.storeProductThumbnail(file, productId);
        String secureUrl = productStorageService.getSecureProductThumbnailUrl(productId);
        metadata.setSecureUrl(secureUrl);

        return ResponseEntity.ok(metadata);
    }
}
