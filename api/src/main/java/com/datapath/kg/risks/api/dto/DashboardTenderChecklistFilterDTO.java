package com.datapath.kg.risks.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardTenderChecklistFilterDTO {
    private int id;
    private String description;
    private String descriptionEn;
}
