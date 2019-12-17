package com.datapath.kg.risks.api.response.dashboard.buyer;

import com.datapath.kg.risks.api.dto.dashboard.buyer.BuyerTopDTO;
import lombok.Data;

import java.util.List;

@Data
public class BuyerTopResponse {
    private List<BuyerTopDTO> data;
}
