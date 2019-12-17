package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;

import java.util.List;

public interface FilterWebService {

    List<Integer> getAvailableIndicatorsForTenderPrioritization(TenderPrioritizationRequest request);
    List<Integer> getAvailableIndicatorsForBuyerPrioritization(BuyerPrioritizationRequest request);
}
