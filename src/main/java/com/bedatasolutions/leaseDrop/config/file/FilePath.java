package com.bedatasolutions.leaseDrop.config.file;

public enum FilePath {
    USERS("users/"), //
    USER_PHOTO("user_photo/"), //
    USER_DOCUMENT("user_document/"), //
    VILLA_GALLERY("villa_gallery/"), //
    ROOM("room/"), //
    BANNER("banner/"),
    BANNER_GALLERY("banner_gallery/"),
    ROOM_GALLERY("room_gallery/"), //
    ROOM_SERVICE("room_service/"), //
    CLUB_PROFILE("club/profile/"); //

    private final String path;

    // private enum constructor
    FilePath(String path) {
        this.path = path;
    }

    public String get() {
        return path;
    }
}
