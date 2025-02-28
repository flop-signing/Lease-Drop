package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "T_DROPDOWN_ITEMS")
public class DropdownItemDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dropdown_item_key")
    private Integer id;

    @Column(name = "id_dropdown_item_ver")
    @Version
    private Integer version;

    @Column(name = "tx_bg_color")
    private String bgColor;

    @Column(name = "tx_description")
    private String description;

    @Column(name = "tx_name")
    private String name;


    @Column(name = "id_sort_order")
    private Integer sortOrder;

    @Column(name = "tx_text_color")
    private String textColor;

    @ManyToOne
    @JoinColumn(name = "id_dropdown_type_key",referencedColumnName = "id_dropdown_type_key")
    private DropdownTypeDao dropdownTypes;


//    @OneToMany(mappedBy = "dropdownItems",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<NotificationDao> notifications;
//
//    @OneToMany(mappedBy = "dropdownItems1",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<NotificationDao> notifications1;
//
//    @OneToMany(mappedBy = "dropdownItems2",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<DocumentDao> documents;
//
//    @OneToMany(mappedBy = "dropdownItems3",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
//    private List<DocumentDao> documents1;

    @OneToOne(fetch = FetchType.EAGER)  // One-to-one relationship with Parent (single parent per dropdown)
    @JoinColumn(name = "id_parent_key", referencedColumnName = "id_dropdown_item_key")  // Nullable to allow no parent
    @ColumnDefault("-2147483648")  // Default value when there's no parent
    private DropdownItemDao parent;

}
