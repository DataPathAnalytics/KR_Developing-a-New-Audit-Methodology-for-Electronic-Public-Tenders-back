package com.datapath.kg.risks.api.dto;

import lombok.Data;

@Data
public class ChecklistIndicatorDTO {

    private Integer id;
    private IndicatorDTO indicator;
    private AnswerTypeDTO answerType;
    private ComponentImpactDTO componentImpact;
    private String comment;

}
