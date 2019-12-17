package com.datapath.kg.risks.api.request;

import lombok.Data;

import java.util.List;

@Data
public class DashboardTenderRequest {
    private Integer tenderId;
    private List<Integer> buyerIds;
    private List<Integer> riskLevel;
    private List<String> procurementMethodDetails;
    private List<String> cpv;
    private List<Integer> indicators;
    private Integer indicatorStatus;
    private String contractStartDate;
    private String contractEndDate;
    private Long completedLotValueMin;
    private Long completedLotValueMax;
    private Integer checklistType;
    private Integer checklistStatus;
    private List<Integer> auditorIds;
}
