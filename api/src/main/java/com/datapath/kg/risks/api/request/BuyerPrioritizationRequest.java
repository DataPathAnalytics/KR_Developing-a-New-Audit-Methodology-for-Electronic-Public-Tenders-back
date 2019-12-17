package com.datapath.kg.risks.api.request;

import com.datapath.kg.risks.api.dto.ColumnDetailsDTO;
import com.datapath.kg.risks.api.dto.SortingOptionDTO;
import lombok.Data;

import java.util.List;

@Data
public class BuyerPrioritizationRequest {

    private List<Integer> buyerIds;
    private List<String> regions;
    private List<String> procurementMethodDetails;
    private List<Integer> riskLevel;
    private List<String> cpv;
    private List<Integer> indicators;
    private String contractStartDate;
    private String contractEndDate;
    private List<SortingOptionDTO> sortingOptions;
    private List<ColumnDetailsDTO> columns;
    private List<Integer> selectedExportIds;
}
