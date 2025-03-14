package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.CustomerDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepo extends JpaRepository<CustomerDao, Integer>, JpaSpecificationExecutor<CustomerDao> {
}

/*
public interface CustomerRepo extends JpaRepository<CustomerDao,Integer> {
 //   Page<CustomerDao> findAll(Pageable pageable);
}
*/
