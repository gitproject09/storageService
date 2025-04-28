package com.supan.storage.controller;

import com.supan.storage.model.FileMetadata;
import com.supan.storage.service.StorageService;
import com.supan.storage.service.StorageTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

/**
 * Controller for generic file operations
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageTokenService tokenService;

    /**
     * Serve a file using token authentication
     */
    @GetMapping("/**")
    public ResponseEntity<InputStreamResource> serveFile(
            @RequestParam("token") String token) {

        try {
            // Validate the token and get the file path
            String filePath = tokenService.validateToken(token);
            if (filePath == null) {
                return ResponseEntity.status(401).build(); // Unauthorized
            }

            // Get the file
            InputStream fileStream = storageService.getFile(filePath);

            // Get metadata
            FileMetadata metadata = storageService.getFileMetadata(filePath);

            // Return the file with appropriate headers
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(metadata.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + metadata.getFilename() + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                    .body(new InputStreamResource(fileStream));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}