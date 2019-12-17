package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.model.dashboard.PrioritizationInfoModel;
import com.datapath.kg.risks.api.dao.model.dashboard.buyer.BuyerTopModel;
import com.datapath.kg.risks.api.request.DashboardBuyerRequest;

import java.util.List;

public interface DashboardBuyerDAOService {
    PrioritizationInfoModel getPrioritizationInfo(DashboardBuyerRequest request);

    List<BuyerTopModel> getTopByRiskTendersCount(DashboardBuyerRequest request);

    List<BuyerTopModel> getTopByRiskTendersAmount(DashboardBuyerRequest request);

    List<BuyerTopModel> getTopByIndicatorsCount(DashboardBuyerRequest request);
}
