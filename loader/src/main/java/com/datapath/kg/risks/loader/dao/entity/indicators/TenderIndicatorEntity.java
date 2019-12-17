package com.datapath.kg.risks.loader.dao.entity.indicators;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "tender_indicator")
@Data
@IdClass(TenderIndicatorId.class)
public class TenderIndicatorEntity {

    @Id
    private Integer tenderId;
    @Id
    private Integer indicatorId;
    private Integer indicatorValue;

}
