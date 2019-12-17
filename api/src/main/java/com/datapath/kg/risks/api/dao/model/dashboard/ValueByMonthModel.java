package com.datapath.kg.risks.api.dao.model.dashboard;

import lombok.Data;

@Data
public class ValueByMonthModel {
    private String date;
    private String value;
    private String valueWithRisk;
}
