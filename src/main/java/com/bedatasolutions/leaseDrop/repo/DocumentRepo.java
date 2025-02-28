package com.bedatasolutions.leaseDrop.repo;


import com.bedatasolutions.leaseDrop.dao.DocumentDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepo extends JpaRepository<DocumentDao,Integer> {
}
