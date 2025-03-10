package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.rmi.dgc.Lease;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "T_USERS")
public class UserDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users_key")
    private Integer id;

    @Column(name = "id_users_ver")
//    @Version
    private Integer version;

//    @Column(name = "id_role_key", nullable = true)
//    private Integer roleId;

    @Column(name = "tx_ldap_uid", unique = true, nullable = false)
    private String ldapUid;

    @Column(name = "tx_password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "tx_email", unique = true, nullable = false)
    private String email;

    @Column(name = "tx_username", unique = true, nullable = false)
    private String username;

//    // One User can have many Analytics
//    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<AnalyticsDao> analytics;
//
//    // One User can have multiple Transactions (One-to-Many relationship)
//    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<TransactionDao> transactions;

//    // Many Users can belong to one Subscription (Many-to-One relationship)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_subscription_key", nullable = false)
//    private SubscriptionDao subscription;

//    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<SubscriptionDao> subscriptions;
//
//    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<DocumentDao>documents;
//
//    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<NotificationDao> notifications;

    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<ContactUsDao> contacts;


    @ManyToMany
    @JoinTable(
            name = "T_MAP_USER_ROLE",
            joinColumns = @JoinColumn(name = "id_users_key"),  // Correct FK column
            inverseJoinColumns = @JoinColumn(name = "id_roles_key") // Correct FK column
    )
    private Set<RolesDao> roles;


}
