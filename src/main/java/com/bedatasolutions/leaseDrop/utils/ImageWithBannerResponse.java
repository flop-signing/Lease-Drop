package com.bedatasolutions.leaseDrop.utils;

import com.bedatasolutions.leaseDrop.dao.BannerDao;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.util.Base64;

public class ImageWithBannerResponse {
    private BannerDao banner;
    private String imageBase64;  // Store the Base64 encoded image
    private int duration;

    public ImageWithBannerResponse(BannerDao banner, Resource image) {
        this.banner = banner;
        this.imageBase64 = encodeImageToBase64(image);
        this.duration = banner.getDuration();  // Assuming 'duration' is an integer
    }

    private String encodeImageToBase64(Resource image) {
        try {
            // Convert image to byte array
            byte[] imageBytes = image.getInputStream().readAllBytes();
            // Convert byte array to Base64 string
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public BannerDao getBanner() {
        return banner;
    }

    public void setBanner(BannerDao banner) {
        this.banner = banner;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
