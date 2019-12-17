package com.datapath.kg.risks.api.dto;

import lombok.Data;

@Data
public class BuyerPrioritizationDTO {

    private Integer id;
    private String identifierId;
    private String identifierLegalNameRu;
    private Integer riskLevel;
    private Integer procedureNumber;
    private Long proceduresExpectedValue;
    private Long contractsAmount;
    private Integer riskedProcedures;
    private Integer riskedProceduresWithContract;
    private Long riskedProceduresExpectedValue;
    private Long riskedProceduresExpectedValueWithContract;
    private Integer proceduresWithBadData;
    private Double weightedRank;

    private Integer passedIndicatorsCount;
    private String passedIndicators;
}