package com.supan.storage.service;

import com.supan.storage.model.FileMetadata;
import io.minio.*;
import io.minio.http.Method;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Core storage service that handles all storage operations
 */
@Service
public class StorageService {
    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    /**
     * Store a file at the specified path
     *
     * @param file The file to store
     * @param path The path where the file should be stored
     * @return Metadata about the stored file
     */
    public FileMetadata storeFile(MultipartFile file, String path) {
        try {
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();
            long size = file.getSize();

            // Upload the file to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );

            inputStream.close();

            // Create and return metadata
            FileMetadata metadata = new FileMetadata();
            metadata.setPath(path);
            metadata.setFilename(FilenameUtils.getName(path));
            metadata.setContentType(contentType);
            metadata.setSize(size);

            return metadata;
        } catch (Exception e) {
            logger.error("Error storing file at path {}: {}", path, e.getMessage(), e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    /**
     * Store a file with auto-generated filename
     *
     * @param file The file to store
     * @param basePath The base path where the file should be stored
     * @return Metadata about the stored file
     */
    public FileMetadata storeFileWithRandomName(MultipartFile file, String basePath) {
        String originalFilename = file.getOriginalFilename();
        String extension = FilenameUtils.getExtension(originalFilename);
        String randomName = UUID.randomUUID().toString();

        if (extension != null && !extension.isEmpty()) {
            randomName += "." + extension;
        }

        String fullPath = basePath + "/" + randomName;
        return storeFile(file, fullPath);
    }

    /**
     * Retrieve a file from storage
     *
     * @param path The path of the file to retrieve
     * @return InputStream of the file
     */
    public InputStream getFile(String path) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
        } catch (Exception e) {
            logger.error("Error retrieving file at path {}: {}", path, e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve file", e);
        }
    }

    /**
     * Check if a file exists
     *
     * @param path The path to check
     * @return true if the file exists, false otherwise
     */
    public boolean fileExists(String path) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Delete a file from storage
     *
     * @param path The path of the file to delete
     * @return true if the file was deleted successfully, false otherwise
     */
    public boolean deleteFile(String path) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );
            return true;
        } catch (Exception e) {
            logger.error("Error deleting file at path {}: {}", path, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Generate a direct download URL with a specified expiration time
     * This bypasses the token system for specific use cases
     *
     * @param path The path of the file
     * @param expiryMinutes How long the URL should be valid (in minutes)
     * @return Presigned URL for direct access
     */
    public String generateDirectDownloadUrl(String path, int expiryMinutes) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(path)
                            .expiry(expiryMinutes, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            logger.error("Error generating direct download URL: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate download URL", e);
        }
    }

    /**
     * Get metadata for a stored file
     *
     * @param path The path of the file
     * @return Metadata about the file
     */
    public FileMetadata getFileMetadata(String path) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path)
                            .build()
            );

            FileMetadata metadata = new FileMetadata();
            metadata.setPath(path);
            metadata.setFilename(FilenameUtils.getName(path));
            metadata.setContentType(stat.contentType());
            metadata.setSize(stat.size());
            metadata.setLastModified(stat.lastModified().toInstant());

            return metadata;
        } catch (Exception e) {
            logger.error("Error getting metadata for file at path {}: {}", path, e.getMessage(), e);
            throw new RuntimeException("Failed to get file metadata", e);
        }
    }

    /**
     * Copy a file from one path to another
     *
     * @param sourcePath The source path
     * @param destinationPath The destination path
     * @return true if the file was copied successfully, false otherwise
     */
    public boolean copyFile(String sourcePath, String destinationPath) {
        try {
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .bucket(bucketName)
                            .object(destinationPath)
                            .source(
                                    CopySource.builder()
                                            .bucket(bucketName)
                                            .object(sourcePath)
                                            .build()
                            )
                            .build()
            );
            return true;
        } catch (Exception e) {
            logger.error("Error copying file from {} to {}: {}", sourcePath, destinationPath, e.getMessage(), e);
            return false;
        }
    }

    /**
     * Move a file from one path to another (copy + delete)
     *
     * @param sourcePath The source path
     * @param destinationPath The destination path
     * @return true if the file was moved successfully, false otherwise
     */
    public boolean moveFile(String sourcePath, String destinationPath) {
        boolean copied = copyFile(sourcePath, destinationPath);
        if (copied) {
            return deleteFile(sourcePath);
        }
        return false;
    }
}
