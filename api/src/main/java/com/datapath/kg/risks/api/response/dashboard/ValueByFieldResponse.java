package com.datapath.kg.risks.api.response.dashboard;

import com.datapath.kg.risks.api.dto.dashboard.ValueByFieldDTO;
import lombok.Data;

import java.util.List;

@Data
public class ValueByFieldResponse {
    private List<ValueByFieldDTO> data;
}
