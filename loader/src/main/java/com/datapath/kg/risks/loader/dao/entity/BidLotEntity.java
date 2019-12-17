package com.datapath.kg.risks.loader.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@Entity(name = "bid_lot")
@IdClass(BidLotId.class)
@AllArgsConstructor
@NoArgsConstructor
public class BidLotEntity {

    @Id
    @Column(name = "bid_id")
    private Integer bidId;
    @Id
    private Integer lotId;

    private Double amount;
}
