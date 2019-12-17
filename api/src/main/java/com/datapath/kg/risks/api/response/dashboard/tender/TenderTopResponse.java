package com.datapath.kg.risks.api.response.dashboard.tender;

import com.datapath.kg.risks.api.dto.dashboard.tender.TenderTopDTO;
import lombok.Data;

import java.util.List;

@Data
public class TenderTopResponse {
    private List<TenderTopDTO> data;
}
