package com.supan.storage.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private boolean nidUploaded;
    private String verificationStatus;

    // Transient fields for image URLs (not stored in DB)
    @Transient
    private String profilePictureUrl;

    @Transient
    private String nidFrontUrl;

    @Transient
    private String nidBackUrl;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNidUploaded() {
        return nidUploaded;
    }

    public void setNidUploaded(boolean nidUploaded) {
        this.nidUploaded = nidUploaded;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getNidFrontUrl() {
        return nidFrontUrl;
    }

    public void setNidFrontUrl(String nidFrontUrl) {
        this.nidFrontUrl = nidFrontUrl;
    }

    public String getNidBackUrl() {
        return nidBackUrl;
    }

    public void setNidBackUrl(String nidBackUrl) {
        this.nidBackUrl = nidBackUrl;
    }
}
