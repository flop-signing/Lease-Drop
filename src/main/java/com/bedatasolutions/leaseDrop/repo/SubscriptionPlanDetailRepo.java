package com.bedatasolutions.leaseDrop.repo;

import com.bedatasolutions.leaseDrop.dao.SubscriptionPlanDetailDao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionPlanDetailRepo extends JpaRepository<SubscriptionPlanDetailDao,Integer> {
}
