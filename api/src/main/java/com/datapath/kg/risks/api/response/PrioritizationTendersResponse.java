package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.TenderPrioritizationDTO;
import lombok.Data;

import java.util.List;

@Data
public class PrioritizationTendersResponse {

    private List<TenderPrioritizationDTO> tenders;

}
