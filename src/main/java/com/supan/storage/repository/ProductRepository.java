package com.supan.storage.repository;

import com.supan.storage.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Product entity
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA automatically provides the basic CRUD operations
    // You can add custom query methods here if needed

    // For example:
    // List<Product> findByNameContaining(String name);
    // List<Product> findByPriceLessThan(Double price);
    // Product findTopByOrderByPriceDesc(); // most expensive product
}
