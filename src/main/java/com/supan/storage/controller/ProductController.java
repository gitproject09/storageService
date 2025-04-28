package com.supan.storage.controller;

import com.supan.storage.model.Product;
import com.supan.storage.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProductsWithImageUrls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductWithImageUrls(id));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") Product product,
            @RequestPart("thumbnail") MultipartFile thumbnail,
            @RequestPart(value = "gallery", required = false) MultipartFile[] gallery) {

        return ResponseEntity.ok(productService.createProduct(product, thumbnail, gallery));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
