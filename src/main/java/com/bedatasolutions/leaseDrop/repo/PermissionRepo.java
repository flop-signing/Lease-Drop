package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.PermissionDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepo extends JpaRepository<PermissionDao,Integer> {
}
