package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_NOTIFICATIONS")
public class NotificationDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notifications_key")
    private Integer id;

    @Column(name = "id_notifications_ver")
    @Version
    private Integer version;

    @Column(name = "tx_content")
    private String content;

    @Column(name = "tx_channel")
    private String channel;

    @Column(name = "tx_subject")
    private String subject;

    @Column(name = "tx_sender")
    private String sender;

    @Column(name = "tx_sender_email", nullable = true)
    private String senderEmail;

    @ManyToOne
    @JoinColumn(name = "id_users_key", referencedColumnName = "id_users_key")  // Foreign key to T_USERS table
    private UserDao users;

    @ManyToOne
    @JoinColumn(name = "id_drd_notification_type_key",referencedColumnName = "id_dropdown_item_key")
    private DropdownItemDao dropdownItems;

    @ManyToOne
    @JoinColumn(name = "id_drd_status_key",referencedColumnName = "id_dropdown_item_key")
    private DropdownItemDao dropdownItems1;

}
