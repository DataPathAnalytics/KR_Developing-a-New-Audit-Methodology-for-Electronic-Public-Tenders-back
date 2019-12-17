package com.datapath.kg.risks.api.dao.model.dashboard;

import lombok.Data;

@Data
public class ValueByFieldModel {
    private String description;
    private Integer indicatorId;
    private Long value;
}
