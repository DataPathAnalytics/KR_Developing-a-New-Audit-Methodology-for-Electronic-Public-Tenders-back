package com.datapath.kg.risks.api.dto.dashboard;

import lombok.Data;

@Data
public class InfoDTO {
    private Long tendersCount;
    private Long tendersAmount;
    private Long riskTendersCount;
    private Long riskTendersAmount;
    private Double riskTendersPercent;
    private Double riskTendersAmountPercent;
    private Long buyersCount;
    private Long riskBuyersCount;
    private Double riskBuyersPercent;
}
