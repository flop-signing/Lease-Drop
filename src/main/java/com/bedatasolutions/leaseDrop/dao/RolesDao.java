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
@Table(name = "T_ROLES")

public class RolesDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_roles_key", nullable = false)
    private Integer id;

    @ManyToMany
    @JoinTable(
            name = "T_MAP_ROLE_PERMISSIONS",
            joinColumns = @JoinColumn(name = "id_roles_key"),  // Correct FK column
            inverseJoinColumns = @JoinColumn(name = "id_permissions_key") // Correct FK column
    )
    private Set<PermissionDao> permissions;

    @ManyToMany(mappedBy = "roles") // Owning side in PermissionDao
    private Set<UserDao> users;

    @Version
    @Column(name = "id_roles_ver")
    private Integer version;
//
//    @Column(name = "tx_action_type")
//    private ActionType actionType;
//
//
//
//    @Column(name = "id_user_mod")
//    private Integer userMod;
//
//    @Column(name = "id_user_added")
//    private Integer userAdded;

    @Column(name = "tx_name")
    private String name;

    @Column(name = "tx_description")
    private String description;

}
