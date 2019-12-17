package com.datapath.kg.risks.loader.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
class BidLotId implements Serializable {

    private Integer bidId;
    private Integer lotId;
}
