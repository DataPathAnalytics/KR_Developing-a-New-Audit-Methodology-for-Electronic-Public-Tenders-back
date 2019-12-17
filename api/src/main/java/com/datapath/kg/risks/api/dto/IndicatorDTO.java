package com.datapath.kg.risks.api.dto;

import lombok.Data;

@Data
public class IndicatorDTO {
    private Integer id;
    private String name;
    private String description;
    private String risks;
    private String lawViolation;
    private String riskLevelText;
    private String descriptionEn;
}