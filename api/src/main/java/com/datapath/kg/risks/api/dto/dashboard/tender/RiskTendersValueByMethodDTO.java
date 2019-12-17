package com.datapath.kg.risks.api.dto.dashboard.tender;

import lombok.Data;

@Data
public class RiskTendersValueByMethodDTO {
    private String method;
    private Long value;
    private Double percent;
}
