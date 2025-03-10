package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.BannerDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepo extends JpaRepository<BannerDao, Integer> {
}
