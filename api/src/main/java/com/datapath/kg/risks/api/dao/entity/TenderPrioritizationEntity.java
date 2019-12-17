package com.datapath.kg.risks.api.dao.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TenderPrioritizationEntity {

    private Integer id;
    private Long procedureExpectedValue;
    private Long completedLotValue;
    private String procurementMethodDetails;
    private LocalDate datePublished;
    private LocalDate contractDate;
    private String contractSigningDate;
    private Integer completedLotsCount;
    private Integer tenderersCount;
    private Integer disqualifiedsCount;
    private Integer suppliersCount;
    private Integer passedIndicatorsCount;
    private Integer cpvCount;
    private Integer riskLevel;
    private Double weightedRank;
    private String identifierId;
    private String identifierLegalNameRu;
    private String passedIndicatorNameList;
    private Integer checklistId;
    private Integer auditorId;
    private String checklistName;
    private String checklistAuditorName;
    private Integer checklistStatus;
}
