package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.DropdownItemDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DropdownItemRepo extends JpaRepository<DropdownItemDao,Integer> {
}
