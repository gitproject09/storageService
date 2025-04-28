package com.supan.storage.service;

import com.supan.storage.constants.StoragePath;
import com.supan.storage.model.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for handling user-related file operations
 */
@Service
public class UserStorageService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageTokenService tokenService;

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Store a user's profile picture
     */
    public FileMetadata storeProfilePicture(MultipartFile file, Long userId) {
        String path = StoragePath.getUserProfilePicturePath(userId);
        return storageService.storeFile(file, path);
    }

    /**
     * Store an NID front image
     */
    public FileMetadata storeNidFront(MultipartFile file, Long userId) {
        String path = StoragePath.getNidFrontPath(userId);
        return storageService.storeFile(file, path);
    }

    /**
     * Store an NID back image
     */
    public FileMetadata storeNidBack(MultipartFile file, Long userId) {
        String path = StoragePath.getNidBackPath(userId);
        return storageService.storeFile(file, path);
    }

    /**
     * Get a secure URL for a user's profile picture
     */
    public String getSecureProfilePictureUrl(Long userId) {
        String path = StoragePath.getUserProfilePicturePath(userId);
        String token = tokenService.generateToken(path);
        return baseUrl + "/api/files/users/" + userId + "/profile-picture?token=" + token;
    }

    /**
     * Get a secure URL for an NID front image
     */
    public String getSecureNidFrontUrl(Long userId) {
        String path = StoragePath.getNidFrontPath(userId);
        String token = tokenService.generateToken(path);
        return baseUrl + "/api/files/users/" + userId + "/nid/front?token=" + token;
    }

    /**
     * Get a secure URL for an NID back image
     */
    public String getSecureNidBackUrl(Long userId) {
        String path = StoragePath.getNidBackPath(userId);
        String token = tokenService.generateToken(path);
        return baseUrl + "/api/files/users/" + userId + "/nid/back?token=" + token;
    }

    /**
     * Delete a user's profile picture
     */
    public boolean deleteProfilePicture(Long userId) {
        String path = StoragePath.getUserProfilePicturePath(userId);
        return storageService.deleteFile(path);
    }

    /**
     * Delete all files related to a user
     */
    public boolean deleteAllUserFiles(Long userId) {
        // Delete profile picture
        deleteProfilePicture(userId);

        // Delete NID images
        String nidFrontPath = StoragePath.getNidFrontPath(userId);
        String nidBackPath = StoragePath.getNidBackPath(userId);
        storageService.deleteFile(nidFrontPath);
        storageService.deleteFile(nidBackPath);

        // You would typically list all files in the user directory and delete them
        // This would require additional MinIO operations
        return true;
    }
}
