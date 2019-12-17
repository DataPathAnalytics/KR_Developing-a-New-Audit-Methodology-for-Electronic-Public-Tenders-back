package com.datapath.kg.risks.api.response.dashboard.tender;

import com.datapath.kg.risks.api.dto.dashboard.tender.TopOkgzByRiskTendersValueDTO;
import lombok.Data;

import java.util.List;

@Data
public class TopOkgzByRiskTendersValueResponse {
    private List<TopOkgzByRiskTendersValueDTO> data;
}
