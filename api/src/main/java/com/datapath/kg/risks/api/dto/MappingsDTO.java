package com.datapath.kg.risks.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class MappingsDTO {

    private List<ProcurementMethodDetailsDTO> procurementMethodDetails;
    private List<RiskLevelDTO> riskLevels;
    private List<IndicatorDTO> indicators;
    private List<RegionDTO> regions;
    private List<AnswerTypeDTO> questionAnswers;
    private List<AnswerTypeDTO> indicatorAnswers;
    private List<ComponentImpactDTO> componentImpacts;
    private List<ChecklistScoreDTO> checklistScores;
    private List<ChecklistStatusDTO> checklistStatuses;
    private List<TemplateTypeDTO> templateTypes;
    private List<OkgzDTO> okgz;
    private List<DashboardTenderChecklistFilterDTO> dashboardTenderChecklistFilter;
    private List<IndicatorStatusDTO> indicatorStatuses;
}
