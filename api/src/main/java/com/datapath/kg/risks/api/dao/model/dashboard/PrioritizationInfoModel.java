package com.datapath.kg.risks.api.dao.model.dashboard;

import lombok.Data;

@Data
public class PrioritizationInfoModel {
    private Long tendersCount;
    private Long tendersAmount;
    private Long riskTendersCount;
    private Long riskTendersAmount;
    private Long buyersCount;
    private Long riskBuyersCount;
}
