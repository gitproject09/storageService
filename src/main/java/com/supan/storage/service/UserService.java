package com.supan.storage.service;

import com.supan.storage.repository.UserRepository;
import com.supan.storage.service.UserStorageService;
import com.supan.storage.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Example user service showing integration with storage service
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStorageService userStorageService;

    /**
     * Update a user's profile picture
     */
    public User updateProfilePicture(Long userId, MultipartFile file) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Store the profile picture
        userStorageService.storeProfilePicture(file, userId);

        // Update user with the new profile picture URL
        String profilePictureUrl = userStorageService.getSecureProfilePictureUrl(userId);
        user.setProfilePictureUrl(profilePictureUrl);

        return userRepository.save(user);
    }

    /**
     * Upload a user's NID images
     */
    public User uploadNidImages(Long userId, MultipartFile frontImage, MultipartFile backImage) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Store NID images
        if (frontImage != null && !frontImage.isEmpty()) {
            userStorageService.storeNidFront(frontImage, userId);
        }

        if (backImage != null && !backImage.isEmpty()) {
            userStorageService.storeNidBack(backImage, userId);
        }

        // Update user's verification status (example business logic)
        user.setNidUploaded(true);
        user.setVerificationStatus("PENDING_REVIEW");

        return userRepository.save(user);
    }

    /**
     * Get a user with secure image URLs
     */
    public User getUserWithImageUrls(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Add profile picture URL
        String profilePictureUrl = userStorageService.getSecureProfilePictureUrl(userId);
        user.setProfilePictureUrl(profilePictureUrl);

        // If the user has uploaded NID, add those URLs (for admin view, for example)
        if (user.isNidUploaded()) {
            String nidFrontUrl = userStorageService.getSecureNidFrontUrl(userId);
            String nidBackUrl = userStorageService.getSecureNidBackUrl(userId);

            user.setNidFrontUrl(nidFrontUrl);
            user.setNidBackUrl(nidBackUrl);
        }

        return user;
    }

    /**
     * Delete a user and associated files
     */
    public void deleteUser(Long userId) {
        // Delete all associated files
        userStorageService.deleteAllUserFiles(userId);

        // Delete the user from the database
        userRepository.deleteById(userId);
    }
}
