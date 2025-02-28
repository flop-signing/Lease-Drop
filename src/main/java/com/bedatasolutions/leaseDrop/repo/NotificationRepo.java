package com.bedatasolutions.leaseDrop.repo;


import com.bedatasolutions.leaseDrop.dao.NotificationDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<NotificationDao,Integer> {
}
