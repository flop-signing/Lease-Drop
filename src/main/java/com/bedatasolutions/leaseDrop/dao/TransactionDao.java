package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_TRANSACTIONS")
public class TransactionDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction_key")
    private Integer id;

    @Column(name = "id_transactions_ver")
//    @Version
    private Integer version;

    @Column(name = "tx_transaction_type")
    private String transactionType;

    @Column(name = "tx_details")
    private String details;

    // Define the relationship with UserDao (One User can have many Transactions)
    @ManyToOne
    @JoinColumn(name = "id_users_key", referencedColumnName = "id_users_key")  // Foreign key to T_USERS table
    private UserDao users;


}
