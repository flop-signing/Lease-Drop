package com.bedatasolutions.leaseDrop.dao;

import com.bedatasolutions.leaseDrop.dao.abstracts.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "T_DOCUMENTS")
public class DocumentDao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documents_key")
    private Integer id;

    @Column(name = "id_documents_ver")
//    @Version
    private Integer version;

    @Column(name = "tx_file_path", nullable = false)
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "id_users_key", referencedColumnName = "id_users_key")  // Foreign key to T_USERS table
    private UserDao users;

    @OneToMany(mappedBy = "documents",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<DocumentSummaryDao> documentSummary;

    @ManyToOne
    @JoinColumn(name = "id_drd_doc_type_key",referencedColumnName = "id_dropdown_item_key")
    private DropdownItemDao dropdownItems2;

    @ManyToOne
    @JoinColumn(name = "id_drd_status_key",referencedColumnName = "id_dropdown_item_key")
    private DropdownItemDao dropdownItems3;

}
