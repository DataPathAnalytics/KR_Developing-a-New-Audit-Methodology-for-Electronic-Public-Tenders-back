package com.datapath.kg.risks.api.dto.dashboard.buyer;

import lombok.Data;

@Data
public class BuyerTopDTO {
    private Integer buyerId;
    private String name;
    private Long riskValue;
    private Long value;
}
