package com.datapath.kg.risks.loader.dto;

import lombok.Data;

import java.util.List;

@Data
public class TenderDTO {
    private Integer id;
    private String status;
    private String currentStage;
    private String date;
    private String datePublished;
    private String procurementMethodRationale;
    private String procurementMethodDetails;
    private ValueDTO value;
    private GuaranteeDTO guarantee;

    private List<LotDTO> lots;
    private List<ItemDTO> items;
    private List<QualificationRequirementDTO> qualificationRequirements;
    private List<EnquiryDTO> enquiries;
    private List<TenderDocumentDTO> documents;
    private String number;
    private PeriodDTO tenderPeriod;
}