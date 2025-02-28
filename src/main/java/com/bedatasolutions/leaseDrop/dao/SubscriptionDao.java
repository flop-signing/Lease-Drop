package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "T_SUBSCRIPTIONS")
public class SubscriptionDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subscriptions_key")
    private Integer id;

    @Column(name = "id_subscription_ver")
//    @Version
    private Integer version;

    @Column(name = "tx_plan_type", nullable = false)
    private String planType;

    @Column(name = "id_document_limit", nullable = false)
    private Integer documentLimit;

    @Column(name = "tx_stripe_customer_id", unique = true, nullable = false)
    private String stripeCustomerId;

    @Column(name = "tx_stripe_subscription_id", unique = true, nullable = false)
    private String stripeSubscriptionId;

    @Column(name = "tx_status", nullable = false)
    private String status;

    // One Subscription can have multiple Users (One-to-Many relationship)
    @ManyToOne
    @JoinColumn(name = "id_users_key", referencedColumnName = "id_users_key")  // Foreign key to T_USERS table
    private UserDao users;
}
