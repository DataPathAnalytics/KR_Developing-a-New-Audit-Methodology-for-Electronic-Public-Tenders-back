package com.datapath.kg.risks.api.response.dashboard.buyer;

import com.datapath.kg.risks.api.dto.dashboard.buyer.BuyerTopByIndicatorCountDTO;
import lombok.Data;

import java.util.List;

@Data
public class BuyerTopByIndicatorCountResponse {
    private List<BuyerTopByIndicatorCountDTO> data;
}
