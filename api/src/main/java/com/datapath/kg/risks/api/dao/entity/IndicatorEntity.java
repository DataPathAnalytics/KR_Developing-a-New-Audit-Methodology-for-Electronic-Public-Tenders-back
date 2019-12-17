package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity(name = "indicator")
public class IndicatorEntity {

    @Id
    private Integer id;
    private String name;
    private String description;
    private String risks;
    private String lawViolation;
    private String riskLevelText;
    private String descriptionEn;
}
