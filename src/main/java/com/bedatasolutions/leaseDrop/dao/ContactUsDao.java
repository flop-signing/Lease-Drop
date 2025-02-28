package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import com.bedatasolutions.leaseDrop.dto.UserDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_CONTACT_US")
public class ContactUsDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_contact_us_key")
    private Integer id;

    @Column(name = "id_contact_us_ver")
    @Version
    private Integer version;

    @Column(name = "tx_name")
    private String name;

    @Column(name = "tx_email")
    private String email;

    @Column(name = "tx_phone")
    private String phone;

    @Column(name = "tx_subject")
    private String subject;

    @Column(name = "tx_message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "id_users_key",referencedColumnName = "id_users_key")
    private UserDao users;


}
