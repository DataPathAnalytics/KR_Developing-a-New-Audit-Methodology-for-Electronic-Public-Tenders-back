package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity(name = "lot")
@EqualsAndHashCode(of = "id")
public class LotEntity {

    @Id
    private Integer id;
    private String status;
    private Double valueAmount;
    private String valueCurrency;
    private String number;

    @ManyToOne(cascade = CascadeType.ALL, fetch = LAZY)
    @JoinColumn(name = "tender_id")
    private TenderEntity tender;

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL)
    private Set<ItemEntity> items = new HashSet<>();

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL)
    private Set<AwardEntity> awards = new HashSet<>();

    @OneToMany(mappedBy = "lot", cascade = CascadeType.ALL)
    private Set<PriceProposalEntity> priceProposal = new HashSet<>();
}
