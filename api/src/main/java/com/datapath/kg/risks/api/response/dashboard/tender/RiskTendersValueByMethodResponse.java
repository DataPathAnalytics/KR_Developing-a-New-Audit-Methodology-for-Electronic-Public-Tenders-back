package com.datapath.kg.risks.api.response.dashboard.tender;

import com.datapath.kg.risks.api.dto.dashboard.tender.RiskTendersValueByMethodDTO;
import lombok.Data;

import java.util.List;

@Data
public class RiskTendersValueByMethodResponse {
    private List<RiskTendersValueByMethodDTO> data;
}
