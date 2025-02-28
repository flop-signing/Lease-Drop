package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.AdminSettingDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSettingRepo extends JpaRepository<AdminSettingDao,Integer> {
}
