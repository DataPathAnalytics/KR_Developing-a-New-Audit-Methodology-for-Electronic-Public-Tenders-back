package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.TenderIndicatorDTO;
import lombok.Data;

import java.util.List;

@Data
public class TenderIndicatorsResponse {

    private List<TenderIndicatorDTO> indicators;

}
