package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "item")
@EqualsAndHashCode(of = "id")
public class ItemEntity {

    @Id
    private Integer id;
    private String classificationId;
    private Double quantity;
    private Integer unitId;
    private String unitName;
    private Double unitValueAmount;
    private String unitValueCurrency;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lot_id")
    private LotEntity lot;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tender_id")
    private TenderEntity tender;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private Set<PriceProposalEntity> priceProposal = new HashSet<>();
}