package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "T_SUBSCRIPTION_PLANS")
public class SubscriptionPlanDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_subscriptions_plans_key")
    private Integer id;


    @Column(name = "id_subscriptions_plans_ver")
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

    @Column(name = "tx_plan_name")
    private String planName;

    @Column(name = "tx_currency")
    private String currency;

    @Column(name = "tx_item")
    private String item;

    @Column(name="tx_price_determination")
    private String priceDetermination;

    @Column(name = "flt_cost", nullable = false, precision = 21, scale = 9)
  //  @ColumnDefault("0.000000000")
    private BigDecimal cost;

    @Column(name = "tx_price_list")
    private String priceList;

    @Column(name = "tx_billing_interval", nullable = false)
   // @ColumnDefault("Day")
    private String billingInterval;

    @Column(name = "id_billing_interval_count", nullable = false)
//    @ColumnDefault("1")
    private int billingIntervalCount;

    @Column(name = "tx_product_price_id")
    private String productPriceId;

    @Column(name = "tx_payment_gateway")
    private String paymentGateway;

    @Column(name = "tx_cost_center")
    private String costCenter;

    @Column(name = "tx_user_tags")
    private String userTags;

    @Column(name = "tx_comments")
    private String comments;

    @Column(name = "tx_assign")
    private String assign;

    @Column(name = "tx_liked_by")
    private String likedBy;

    @OneToOne(mappedBy = "subscriptionPlan",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private SubscriptionPlanDetailDao subscriptionPlanDetail;



}
