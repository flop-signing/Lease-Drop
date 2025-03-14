package com.bedatasolutions.leaseDrop.dao;


import com.bedatasolutions.leaseDrop.constants.db.FieldType;
import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

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


    @Column(name = "tx_type")
    @Enumerated(EnumType.STRING)
    FieldType type;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("false")
    private boolean isActive;


    // Storing the list as a CSV string in the database
    @Column(name = "tx_relation", columnDefinition = "TEXT")
    private String relation;  // This is the CSV string

    // Convert the List<String> to a comma-separated string
    public void setRelationList(List<String> relationList) {
        this.relation = String.join(",", relationList);  // Convert List to CSV
    }

    // Convert the CSV string back to List<String>
    public List<String> getRelationList() {
        return List.of(this.relation.split(","));  // Convert CSV back to List
    }


}