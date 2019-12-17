package com.datapath.kg.risks.api.response.dashboard;

import com.datapath.kg.risks.api.dto.dashboard.ValueByMonthDTO;
import lombok.Data;

import java.util.List;

@Data
public class ByMonthResponse {
    private List<ValueByMonthDTO> data;
}
