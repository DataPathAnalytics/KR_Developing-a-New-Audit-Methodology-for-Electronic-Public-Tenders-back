package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;

import java.util.List;

public interface FilterDAOService {

    List<Integer> getAvailableIndicatorsForTenderPrioritization(TenderPrioritizationRequest request);
    List<Integer> getAvailableIndicatorsForBuyerPrioritization(BuyerPrioritizationRequest request);
}
