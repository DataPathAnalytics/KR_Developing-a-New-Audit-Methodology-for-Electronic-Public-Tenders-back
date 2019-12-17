package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.BuyerPrioritizationDTO;
import lombok.Data;

import java.util.List;

@Data
public class PrioritizationBuyersResponse {

    private List<BuyerPrioritizationDTO> buyers;

}
