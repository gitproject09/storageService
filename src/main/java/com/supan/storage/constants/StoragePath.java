package com.supan.storage.constants;

/**
 * Constants for storage paths to ensure consistency across the application
 */
public final class StoragePath {
    // Base folders
    public static final String PRODUCT_IMAGES = "products";
    public static final String USER_IMAGES = "users";
    public static final String DOCUMENTS = "documents";
    public static final String TEMPORARY = "temp";

    // User-related subfolders
    public static final String PROFILE_PICTURES = USER_IMAGES + "/profile";
    public static final String USER_DOCUMENTS = USER_IMAGES + "/documents";

    // Document-specific paths
    public static final String IDENTITY_DOCUMENTS = DOCUMENTS + "/identity";
    public static final String NID_FRONT = IDENTITY_DOCUMENTS + "/nid/front";
    public static final String NID_BACK = IDENTITY_DOCUMENTS + "/nid/back";
    public static final String PASSPORT = IDENTITY_DOCUMENTS + "/passport";

    // Product-related subfolders
    public static final String PRODUCT_THUMBNAILS = PRODUCT_IMAGES + "/thumbnails";
    public static final String PRODUCT_GALLERY = PRODUCT_IMAGES + "/gallery";

    private StoragePath() {
        // Private constructor to prevent instantiation
    }

    /**
     * Generate a path for a user's profile picture
     */
    public static String getUserProfilePicturePath(Long userId) {
        return PROFILE_PICTURES + "/" + userId + "/profile.jpg";
    }

    /**
     * Generate a path for a product image
     */
    public static String getProductImagePath(Long productId, String filename) {
        return PRODUCT_IMAGES + "/" + productId + "/" + filename;
    }

    /**
     * Generate a path for a product thumbnail
     */
    public static String getProductThumbnailPath(Long productId) {
        return PRODUCT_THUMBNAILS + "/" + productId + "/thumbnail.jpg";
    }

    /**
     * Generate a path for an NID front image
     */
    public static String getNidFrontPath(Long userId) {
        return NID_FRONT + "/" + userId + "/front.jpg";
    }

    /**
     * Generate a path for an NID back image
     */
    public static String getNidBackPath(Long userId) {
        return NID_BACK + "/" + userId + "/back.jpg";
    }
}
