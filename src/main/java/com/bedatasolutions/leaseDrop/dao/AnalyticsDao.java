package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_ANALYTICS")
public class AnalyticsDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_analytics_key")
    private Integer id;

    @Column(name = "id_analytics_ver")
    @Version
    private Integer version;

    @Column(name = "tx_data", nullable = false)
    private String data;

    @ManyToOne
    @JoinColumn(name = "id_users_key", referencedColumnName = "id_users_key")
    private UserDao users;

}
