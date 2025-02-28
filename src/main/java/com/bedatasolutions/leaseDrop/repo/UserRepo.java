package com.bedatasolutions.leaseDrop.repo;


import com.bedatasolutions.leaseDrop.dao.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserDao, Integer> {
}
