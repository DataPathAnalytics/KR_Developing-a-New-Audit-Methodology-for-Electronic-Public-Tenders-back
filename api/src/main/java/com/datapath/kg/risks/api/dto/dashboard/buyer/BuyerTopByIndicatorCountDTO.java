package com.datapath.kg.risks.api.dto.dashboard.buyer;

import lombok.Data;

@Data
public class BuyerTopByIndicatorCountDTO {
    private Integer buyerId;
    private String name;
    private Long value;
}
