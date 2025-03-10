package com.bedatasolutions.leaseDrop.dao;
import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_SUBSCRIPTION_PLAN_DETAILS")
public class SubscriptionPlanDetailDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_subscription_plan_details_key")
    private Integer id;

    @Column(name = "id_subscription_plan_details_ver")
    @Version
    private Integer version;

    @Column(name = "tx_name")
    private String name;

    @Column(name = "tx_owner")
    private String owner;

    @Column(name = "tx_docstatus")
    private Integer docstatus;

    @Column(name = "id_idx")
    private Integer idx;

    @Column(name = "tx_plan")
    private String plan;

    @Column(name = "id_qty")
   // @ColumnDefault("0")
    private Integer qty;

    @Column(name = "tx_parent")
    private String parent;

    @Column(name = "tx_parentfield")
    private String parentField;

    @Column(name = "tx_parenttype")
    private String parentType;

    @OneToOne
    @JoinColumn(name = "id_subscriptions_plans_key", referencedColumnName = "id_subscriptions_plans_key")
    private SubscriptionPlanDao subscriptionPlan;



}


