package com.supan.storage.service;

import com.supan.storage.model.Product;
import com.supan.storage.repository.ProductRepository;
import com.supan.storage.service.ProductStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Example product service showing integration with storage service
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStorageService productStorageService;

    /**
     * Create a new product with images
     */
    public Product createProduct(Product product, MultipartFile mainImage, MultipartFile[] galleryImages) {
        // First save the product to get an ID
        Product savedProduct = productRepository.save(product);
        Long productId = savedProduct.getId();

        // Store the main image as thumbnail
        if (mainImage != null && !mainImage.isEmpty()) {
            productStorageService.storeProductThumbnail(mainImage, productId);
        }

        // Store gallery images
        if (galleryImages != null) {
            for (MultipartFile image : galleryImages) {
                if (image != null && !image.isEmpty()) {
                    productStorageService.storeProductImage(image, productId);
                }
            }
        }

        // Return the product with image URLs
        return getProductWithImageUrls(productId);
    }

    /**
     * Get a product with secure image URLs
     */
    public Product getProductWithImageUrls(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // Add thumbnail URL
        String thumbnailUrl = productStorageService.getSecureProductThumbnailUrl(productId);
        product.setThumbnailUrl(thumbnailUrl);

        // In a real implementation, you would retrieve all gallery images and add their URLs
        // This is a simplified example

        return product;
    }

    /**
     * Get all products with image URLs
     */
    public List<Product> getAllProductsWithImageUrls() {
        List<Product> products = productRepository.findAll();

        // Add image URLs to each product
        for (Product product : products) {
            String thumbnailUrl = productStorageService.getSecureProductThumbnailUrl(product.getId());
            product.setThumbnailUrl(thumbnailUrl);
        }

        return products;
    }

    /**
     * Delete a product and its images
     */
    public void deleteProduct(Long productId) {
        // Delete all associated files
        productStorageService.deleteAllProductFiles(productId);

        // Delete the product from the database
        productRepository.deleteById(productId);
    }
}

