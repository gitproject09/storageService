package com.supan.storage.repository;

import com.supan.storage.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring Data JPA automatically provides the basic CRUD operations
    // You can add custom query methods here if needed

    // For example:
    // Optional<User> findByUsername(String username);
    // Optional<User> findByEmail(String email);
    // List<User> findByVerificationStatus(String verificationStatus);
    // boolean existsByUsername(String username);
    // boolean existsByEmail(String email);
}