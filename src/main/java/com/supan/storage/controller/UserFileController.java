package com.supan.storage.controller;

import com.supan.storage.model.FileMetadata;
import com.supan.storage.service.UserStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for user-specific file operations
 */
@RestController
@RequestMapping("/api/users/{userId}/files")
public class UserFileController {

    @Autowired
    private UserStorageService userStorageService;

    /**
     * Upload a user profile picture
     */
    @PostMapping("/profile-picture")
    public ResponseEntity<FileMetadata> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        FileMetadata metadata = userStorageService.storeProfilePicture(file, userId);
        String secureUrl = userStorageService.getSecureProfilePictureUrl(userId);
        metadata.setSecureUrl(secureUrl);

        return ResponseEntity.ok(metadata);
    }

    /**
     * Upload an NID front image
     */
    @PostMapping("/nid/front")
    public ResponseEntity<FileMetadata> uploadNidFront(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        FileMetadata metadata = userStorageService.storeNidFront(file, userId);
        String secureUrl = userStorageService.getSecureNidFrontUrl(userId);
        metadata.setSecureUrl(secureUrl);

        return ResponseEntity.ok(metadata);
    }

    /**
     * Upload an NID back image
     */
    @PostMapping("/nid/back")
    public ResponseEntity<FileMetadata> uploadNidBack(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) {

        FileMetadata metadata = userStorageService.storeNidBack(file, userId);
        String secureUrl = userStorageService.getSecureNidBackUrl(userId);
        metadata.setSecureUrl(secureUrl);

        return ResponseEntity.ok(metadata);
    }
}
