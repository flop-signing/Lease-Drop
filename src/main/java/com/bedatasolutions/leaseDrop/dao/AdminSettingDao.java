package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.CookieValue;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "T_ADMIN_SETTINGS")
public class AdminSettingDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_admin_settings_key")
    private Integer id;

    @Column(name = "id_admin_settings_ver")
    private Integer version;

//
//    @Column(name = "tx_action_type")
//    private String actionType;

//    @Column(name = "id_user_mod")
//    private Integer userMod;
//
//    @Column (name = "id_user_added")
//    private Integer userAdded;

    @Column(name = "tx_settings_key")
    private String settingsKey;

    @Column(name = "tx_setting_value")
    private String settingValue;

    @Column(name = "is_email_notifications")
    private Boolean emailNotifications;

    @Column(name = "ct_temp_file_duration")
    private Integer tempFileDuration;

    @Column(name="ct_cache_duration")
    private Integer cacheDuration;

    @Column(name = "ct_subscription_remainder_duration")
    private Integer subscriptionRemainderDuration;

    @Column(name = "is_welcome_mail")
    private Boolean welcomeMail;

    @Column(name = "ct_otp_expiry")
    private Integer otpExpiry;

}
