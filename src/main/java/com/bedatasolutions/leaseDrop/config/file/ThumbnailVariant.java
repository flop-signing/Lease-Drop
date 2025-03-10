package com.bedatasolutions.leaseDrop.config.file;

public enum ThumbnailVariant {
    L("temp/100", 100),
    M("temp/200", 200),
    O("/", 0);
    private String path;
    private Integer width;

    ThumbnailVariant(String path, Integer width) {
        this.path = path;
        this.width = width;
    }

    public String path() {
        return path;
    }

    public Integer width() {
        return width;
    }
}