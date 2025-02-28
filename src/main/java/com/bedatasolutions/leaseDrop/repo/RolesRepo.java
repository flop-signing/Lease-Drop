package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.RolesDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepo extends JpaRepository<RolesDao,Integer> {
}
