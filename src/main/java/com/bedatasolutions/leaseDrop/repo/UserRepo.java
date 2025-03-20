package com.bedatasolutions.leaseDrop.repo;


import com.bedatasolutions.leaseDrop.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepo extends JpaRepository<UserDao, Integer>, JpaSpecificationExecutor<UserDao> {
}
