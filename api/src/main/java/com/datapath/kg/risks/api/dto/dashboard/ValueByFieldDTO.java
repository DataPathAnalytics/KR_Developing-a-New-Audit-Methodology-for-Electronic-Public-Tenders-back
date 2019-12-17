package com.datapath.kg.risks.api.dto.dashboard;

import lombok.Data;

@Data
public class ValueByFieldDTO {
    private String description;
    private Integer indicatorId;
    private Long value;
}
