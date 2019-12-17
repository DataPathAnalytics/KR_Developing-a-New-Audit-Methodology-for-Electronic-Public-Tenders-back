package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity(name = "qualification_requirement")
public class QualificationRequirementEntity {

    @Id
    private Integer id;
    private String type;

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "tender_id")
    private TenderEntity tender;

}
