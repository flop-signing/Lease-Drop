package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.ClassifierDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassifierRepo extends JpaRepository<ClassifierDao,Integer> {
}
