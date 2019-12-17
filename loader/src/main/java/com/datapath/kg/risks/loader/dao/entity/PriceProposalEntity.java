package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "price_proposal")
public class PriceProposalEntity {

    @Id
    private Integer id;
    private Integer unitId;
    private String unitName;
    private Double unitValueAmount;
    private String unitValueCurrency;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lot_id")
    private LotEntity lot;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bid_id")
    private BidEntity bid;

}