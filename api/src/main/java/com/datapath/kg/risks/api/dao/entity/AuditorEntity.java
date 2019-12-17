package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "auditor")
@Data
public class AuditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String name;
    private boolean disabled;
    private boolean accountLocked;
    private Float tenderValueRank;
    private Float tenderRiskLevelRank;
    private Float buyerValueRank;
    private Float buyerRiskLevelRank;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "auditor_permission",
            joinColumns = {@JoinColumn(name = "auditor_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"auditor_id", "permission_id"}))
    private List<PermissionEntity> permissions;
}
