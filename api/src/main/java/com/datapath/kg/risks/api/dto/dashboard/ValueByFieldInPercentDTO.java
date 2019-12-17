package com.datapath.kg.risks.api.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValueByFieldInPercentDTO {
    private String description;
    private Integer indicatorId;
    private Double value;
}
