package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.SubscriptionPlanDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanRepo extends JpaRepository<SubscriptionPlanDao,Integer> {
}
