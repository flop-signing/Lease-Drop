package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_DOCUMENT_SUMMARIES")
public class
DocumentSummaryDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_document_summaries_key")
    private Integer id;

    @Column(name = "id_document_summaries_ver")
    @Version
    private Integer version;

    @Column(name = "tx_summary", nullable = false)
    private String summary;

    @Column(name = "tx_metadata", nullable = false)
    private String metaData;

    @ManyToOne
    @JoinColumn(name = "id_documents_key",referencedColumnName = "id_documents_key")
    private DocumentDao documents;
}
