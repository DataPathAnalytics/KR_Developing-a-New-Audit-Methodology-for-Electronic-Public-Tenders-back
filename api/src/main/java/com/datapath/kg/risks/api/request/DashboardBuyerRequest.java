package com.datapath.kg.risks.api.request;

import lombok.Data;

import java.util.List;

@Data
public class DashboardBuyerRequest {
    private List<Integer> buyerIds;
    private List<Integer> riskLevel;
    private List<String> procurementMethodDetails;
    private List<String> cpv;
    private List<Integer> indicators;
    private String contractStartDate;
    private String contractEndDate;
    private List<String> regions;
}
