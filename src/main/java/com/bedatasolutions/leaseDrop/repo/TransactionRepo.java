package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.TransactionDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<TransactionDao,Integer> {
}
