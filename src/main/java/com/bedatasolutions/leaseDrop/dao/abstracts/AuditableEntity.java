package com.bedatasolutions.leaseDrop.dao.abstracts;


import com.bedatasolutions.leaseDrop.constants.db.ActionType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity {

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("true")
    private Boolean isActive = true;

    @Column(name = "id_action_key", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ActionType actionKey;

    @Column(name = "id_env_key", nullable = false)
    @ColumnDefault("100000")
    private Integer environmentKey=100000;

    @Column(name = "id_evt_key")
    @ColumnDefault("100000")
    private Integer eventKey=100000;

    @Column(name = "id_state_key", nullable = false)
    @ColumnDefault("100000")
    private Integer stateKey = 100000;

    @Column(name = "tx_user_added", length = 140, nullable = false)
    @CreatedBy
    private UUID createdBy = UUID.randomUUID();

    @Column(name = "tx_user_mod", length = 140, nullable = false)
    @LastModifiedBy
    private UUID modifiedBy = UUID.randomUUID();

    @Column(name = "dtt_added", nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime createdAt;

    @Column(name = "dtt_mod", nullable = false)
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime modifiedAt;
}
