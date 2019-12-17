package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Data
@Entity(name = "related_process")
    public class RelatedProcessEntity {

    @Id
    private String id;
    private Integer identifier;
    private String relationship;

    @ManyToOne(cascade = ALL, fetch = LAZY)
    @JoinColumn(name = "tender_id")
    private TenderEntity tender;
}
