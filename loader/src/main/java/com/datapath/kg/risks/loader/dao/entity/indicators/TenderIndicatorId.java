package com.datapath.kg.risks.loader.dao.entity.indicators;


import lombok.Data;

import java.io.Serializable;

@Data
public class TenderIndicatorId implements Serializable {

    private Integer tenderId;
    private Integer indicatorId;

}
