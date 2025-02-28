package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.SoftwareConfigurationDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoftwareConfigurationRepo extends JpaRepository<SoftwareConfigurationDao,Integer> {
}
