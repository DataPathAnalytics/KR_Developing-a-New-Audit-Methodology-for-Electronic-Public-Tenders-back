package com.datapath.kg.risks.api.response.dashboard;

import com.datapath.kg.risks.api.dto.dashboard.ValueByFieldInPercentDTO;
import lombok.Data;

import java.util.List;

@Data
public class ValueByFieldInPercentResponse {
    private List<ValueByFieldInPercentDTO> data;
}
