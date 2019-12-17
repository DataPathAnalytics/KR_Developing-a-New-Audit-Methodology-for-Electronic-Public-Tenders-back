package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.BuyerDTO;
import lombok.Data;

import java.util.List;

@Data
public class BuyersResponse {

    private List<BuyerDTO> buyers;

}
