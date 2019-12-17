package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Data
@Entity(name = "award")
@EqualsAndHashCode(of = "id")
public class AwardEntity {

    @Id
    private Integer id;
    private LocalDateTime date;
    private String status;
    private Double valueAmount;
    private String valueCurrency;

    @OneToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "bid_id")
    private BidEntity bid;

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "lot_id")
    private LotEntity lot;

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "tender_id")
    private TenderEntity tender;
//
//    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "awards")
//    private Set<ContractEntity> contracts = new HashSet<>();
}
