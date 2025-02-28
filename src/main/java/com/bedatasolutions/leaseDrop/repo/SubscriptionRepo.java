package com.bedatasolutions.leaseDrop.repo;


import com.bedatasolutions.leaseDrop.dao.SubscriptionDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<SubscriptionDao,Integer> {
}
