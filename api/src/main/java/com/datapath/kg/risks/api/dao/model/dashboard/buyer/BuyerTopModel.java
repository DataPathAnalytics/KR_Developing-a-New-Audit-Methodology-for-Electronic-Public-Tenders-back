package com.datapath.kg.risks.api.dao.model.dashboard.buyer;

import lombok.Data;

@Data
public class BuyerTopModel {
    private Integer buyerId;
    private String name;
    private Long riskValue;
    private Long value;
}
