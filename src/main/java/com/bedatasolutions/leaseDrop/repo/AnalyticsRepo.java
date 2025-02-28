package com.bedatasolutions.leaseDrop.repo;


import com.bedatasolutions.leaseDrop.dao.AnalyticsDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalyticsRepo extends JpaRepository<AnalyticsDao,Integer> {
}
