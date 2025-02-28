package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_SOFTWARE_CONFIGURATION")

public class SoftwareConfigurationDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_software_configuration_key")
    private Integer id;

    @Column(name = "id_software_configuration_ver")
    @Version
    private Integer version;

    @Column(name = "tx_config_key")
    private String configKey;

    @Column(name = "tx_config_value")
    private String configValue;


}
