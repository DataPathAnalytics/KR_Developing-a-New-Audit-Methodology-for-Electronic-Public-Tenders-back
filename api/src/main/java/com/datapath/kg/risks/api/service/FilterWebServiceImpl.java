package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.dao.service.FilterDAOService;
import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterWebServiceImpl implements FilterWebService {

    private FilterDAOService service;

    public FilterWebServiceImpl(FilterDAOService service) {
        this.service = service;
    }

    @Override
    public List<Integer> getAvailableIndicatorsForTenderPrioritization(TenderPrioritizationRequest request) {
        return service.getAvailableIndicatorsForTenderPrioritization(request);
    }

    @Override
    public List<Integer> getAvailableIndicatorsForBuyerPrioritization(BuyerPrioritizationRequest request) {
        return service.getAvailableIndicatorsForBuyerPrioritization(request);
    }
}
