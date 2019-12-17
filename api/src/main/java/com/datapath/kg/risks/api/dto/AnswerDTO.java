package com.datapath.kg.risks.api.dto;

import lombok.Data;

@Data
public class AnswerDTO {

    private Integer id;
    private String comment;
    private String npa;

    private AnswerTypeDTO answerType;
    private ComponentImpactDTO componentImpact;

    private String categoryName;
    private String categoryNumber;
    private String questionDescription;
    private boolean baseQuestion;
    private String questionNumber;

}