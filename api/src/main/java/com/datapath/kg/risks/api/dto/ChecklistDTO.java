package com.datapath.kg.risks.api.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChecklistDTO {

    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate modifiedDate;
    private String tendersComment;
    private ComponentImpactDTO tendersImpact;

    private ChecklistScoreDTO autoScore;
    private ChecklistScoreDTO manualScore;
    private ChecklistScoreDTO manualTendersScore;
    private ChecklistScoreDTO autoTendersScore;
    private List<AnswerDTO> answers;
    private List<ChecklistIndicatorDTO> indicators;

    private ChecklistStatusDTO status;
    private BuyerDTO buyer;
    private AuditorDTO auditor;
    private String summary;
    private String auditName;
    private String contractNumber;
    private Double contractAmount;
    private String contractDescription;
    private String supplier;
    private String templateName;
    private Integer templateTypeId;
    private TenderDTO tender;
}