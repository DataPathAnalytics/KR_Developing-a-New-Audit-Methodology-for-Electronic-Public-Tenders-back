package com.datapath.kg.risks.api.request;

import com.datapath.kg.risks.api.dto.ColumnDetailsDTO;
import com.datapath.kg.risks.api.dto.SortingOptionDTO;
import lombok.Data;

import java.util.List;

@Data
public class TenderPrioritizationRequest {

    private List<Integer> buyerIds;
    private Integer tenderId;
    private List<String> procurementMethodDetails;
    private List<Integer> riskLevel;
    private List<String> cpv;
    private Long completedLotValueMin;
    private Long completedLotValueMax;
    private List<Integer> indicators;
    private Integer indicatorStatus;
    private String contractStartDate;
    private String contractEndDate;
    private List<SortingOptionDTO> sortingOptions;
    private List<ColumnDetailsDTO> columns;
    private List<Integer> selectedExportIds;
    private Integer checklistType;
    private Integer checklistStatus;
    private List<Integer> auditorIds;
}
