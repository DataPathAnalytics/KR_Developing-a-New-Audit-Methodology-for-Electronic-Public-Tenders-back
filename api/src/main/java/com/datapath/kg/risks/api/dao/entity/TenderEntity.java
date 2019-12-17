package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "tender")
public class TenderEntity {

    @Id
    private Integer id;
    private String number;
    private LocalDate datePublished;
    private LocalDate periodStartDate;
    private LocalDate periodEndDate;
    private Double guaranteeAmount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tender_id")
    private Set<BidEntity> bids = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "tender_id")
    private Set<ContractEntity> contracts = new HashSet<>();
}
