package com.supan.storage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.Instant;

/**
 * Model to represent file metadata
 */
public class FileMetadata {
    @JsonIgnore
    private String path; // Internal path, not exposed to clients

    private String filename;
    private String contentType;
    private long size;
    private Instant lastModified;
    private String secureUrl; // Secure URL for client access

    // Getters and setters
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public void setSecureUrl(String secureUrl) {
        this.secureUrl = secureUrl;
    }
}

