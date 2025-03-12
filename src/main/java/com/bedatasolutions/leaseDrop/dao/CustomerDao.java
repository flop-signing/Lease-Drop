package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "T_CUSTOMERS")
public class CustomerDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_cutomer_key")
    private Integer id;

    @Column(name = "id_customer_ver")
    @Version
    private Integer version;

    @Column(name = "tx_name")
    private String name;

    @Column(name = "tx_package_type")
    private String packageType;

    @Column(name = "tx_amount")
    private String amount;

    @Column(name = "dt_purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "dt_expire_date")
    private LocalDate expireDate;

    @Column(name = "tx_remaining_days")
    private String remainingDays;

    @Column(name = "id_file_processing")
    private Integer fileProcessing;





}
