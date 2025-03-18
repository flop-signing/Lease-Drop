package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.BannerDao;
import com.bedatasolutions.leaseDrop.dao.CustomerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BannerRepo extends JpaRepository<BannerDao, Integer>, JpaSpecificationExecutor<BannerDao> {
}
