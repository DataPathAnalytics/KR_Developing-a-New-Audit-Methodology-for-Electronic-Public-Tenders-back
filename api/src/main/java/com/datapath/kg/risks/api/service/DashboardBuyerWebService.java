package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.dto.dashboard.InfoDTO;
import com.datapath.kg.risks.api.request.DashboardBuyerRequest;
import com.datapath.kg.risks.api.response.dashboard.buyer.BuyerTopByIndicatorCountResponse;
import com.datapath.kg.risks.api.response.dashboard.buyer.BuyerTopResponse;

public interface DashboardBuyerWebService {
    InfoDTO getInfo(DashboardBuyerRequest request);

    BuyerTopResponse getTopByRiskTendersCount(DashboardBuyerRequest request);

    BuyerTopResponse getTopByRiskTendersAmount(DashboardBuyerRequest request);

    BuyerTopByIndicatorCountResponse getTopByIndicatorsCount(DashboardBuyerRequest request);
}
