package com.datapath.kg.risks.api.dto;

import lombok.Data;

@Data
public class AuditorSettingsDTO {
    private Float tenderValueRank;
    private Float tenderRiskLevelRank;
    private Float buyerValueRank;
    private Float buyerRiskLevelRank;
}
