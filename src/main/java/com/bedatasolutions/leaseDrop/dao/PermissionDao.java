package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "T_PERMISSIONS")
public class PermissionDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_permissions_key")
    private Integer id;


    @Column(name = "id_permissions_ver")
    @Version
    private Integer version;
//
//    @Column(name = "tx_action_type", nullable = true)
//    @Enumerated(EnumType.ORDINAL)
//    private ActionType actionType;


//    @Column(name = "id_user_mod")
//    private Integer userMod;
//
//    @Column(name = "id_user_added")
//    private Integer userAdded;

    @Column(name = "tx_name")
    private String name;

    @Column(name = "tx_description")
    private String description;

    @ManyToMany(mappedBy = "permissions") // Owning side in PermissionDao
    private Set<RolesDao> roles;

}
