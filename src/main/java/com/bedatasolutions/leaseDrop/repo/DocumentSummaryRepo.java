package com.bedatasolutions.leaseDrop.repo;


import com.bedatasolutions.leaseDrop.dao.DocumentSummaryDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentSummaryRepo extends JpaRepository<DocumentSummaryDao,Integer> {
}
