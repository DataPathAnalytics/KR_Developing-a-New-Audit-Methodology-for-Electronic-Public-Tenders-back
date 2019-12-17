package com.datapath.kg.risks.api.dto.dashboard;

import lombok.Data;

@Data
public class ValueByMonthDTO {
    private String date;
    private String value;
    private String valueWithRisk;
}
