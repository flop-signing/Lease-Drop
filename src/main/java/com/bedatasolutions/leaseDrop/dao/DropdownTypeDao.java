package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "T_DROPDOWN_TYPES")
public class DropdownTypeDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dropdown_type_key")
    private Integer id;

    @Column(name = "id_dropdown_type_ver")
    @Version
    private Integer version;

    @Column(name = "tx_description")
    private String description;

    @Column(name = "tx_name")
    private String name;

    @Column(name = "id_sort_order")
    private Integer sortOrder;


//   @OneToMany(mappedBy = "dropdownTypes",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//   List<DropdownItemDao> dropdownItems;

    @OneToOne(fetch = FetchType.EAGER)  // One-to-one relationship with Parent (single parent per dropdown)
    @JoinColumn(name = "id_parent_key", referencedColumnName = "id_dropdown_type_key")  // Nullable to allow no parent
    @ColumnDefault("-2147483648")  // Default value when there's no parent
    private DropdownTypeDao parent;


    @OneToOne(fetch = FetchType.EAGER)  // One-to-one relationship with Child (single child per dropdown)
    @JoinColumn(name = "id_child_key", referencedColumnName = "id_dropdown_type_key")  // Nullable to allow no child
    @ColumnDefault("-2147483648")  // Default value when there's no child
    private DropdownTypeDao child;




}
