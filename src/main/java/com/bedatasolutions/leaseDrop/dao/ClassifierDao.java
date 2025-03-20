package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.constants.db.FieldType;
import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Entity
@Table(name = "T_CLASSIFIER")
@Getter
@Setter
public class ClassifierDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_classifier_key")
    private Integer id;

    @Column(name = "id_classifier_ver")
    private Integer version;

    @Column(name = "tx_name", length = 100)
    private String name;

    @Column(name = "tx_group_key", length = 100)
    private String groupKey;

    @Column(name = "tx_description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "tx_type")
    private FieldType type;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("false")
    private boolean isActive;

    // Store relation as a serialized List<String> (CSV or JSON format)
    @Column(name = "tx_relation", columnDefinition = "TEXT")
    private String relation;  // This holds the serialized List<String> as a CSV string

    // Serialize the List<String> into a CSV string before saving
    @PrePersist
    public void prePersist() {
        if (relation != null && !relation.isEmpty()) {
            // Serialize List<String> to CSV string
            this.relation = String.join(",", relation);  // Convert List<String> to CSV
        }
    }

    // Convert CSV string to List<String> after retrieval
    public List<String> getRelationList() {
        if (relation == null || relation.isEmpty()) {
            return List.of();  // Return an empty list if relation is null or empty
        }
        return Arrays.asList(relation.split(","));  // Convert CSV string back to List<String>
    }

}
