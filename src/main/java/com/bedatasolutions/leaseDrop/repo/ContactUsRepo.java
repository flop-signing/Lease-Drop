package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.ContactUsDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactUsRepo extends JpaRepository<ContactUsDao,Integer> {
}
