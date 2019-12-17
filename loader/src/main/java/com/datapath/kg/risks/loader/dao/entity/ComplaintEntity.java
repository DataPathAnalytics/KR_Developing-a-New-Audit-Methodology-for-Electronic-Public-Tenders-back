package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "complaint")
public class ComplaintEntity {

    @Id
    private Integer id;
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tender_id")
    private TenderEntity tender;
}
