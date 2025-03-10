package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "T_BANNERS")
public class BannerDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_banners_key")
    private Integer id;

    @Column(name = "id_banners_ver")
    private Integer version;

    @Column(name = "tx_title")
    private String title;

    @Column(name = "tx_size")
    private String size;// Size of the image

    @Column(name = "tx_file_path")
    private String filePath;

//    @Column(name = "dtt_upload_date")
//    private LocalDateTime uploadDate;

    @Column(name="id_duration")
    private Integer duration;  // Duration for the image in sec


}
