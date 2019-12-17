package com.datapath.kg.risks.api.request;

import lombok.Data;

@Data
public class DashboardBaseRequest {
    private String contractStartDate;
    private String contractEndDate;
}
