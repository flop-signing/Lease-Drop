package com.bedatasolutions.leaseDrop.utils;

import com.bedatasolutions.leaseDrop.dao.BannerDao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Getter
@Setter

public class ImageWithBannerResponse {
        private BannerDao banner;
        private Resource image;
        private int duration; // Assuming 'duration' is an integer, adjust type if needed



    public ImageWithBannerResponse(BannerDao banner, Resource image) {
        this.banner = banner;
        this.image = image;
        this.duration = banner.getDuration();
    }
}
