package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;
import com.datapath.kg.risks.api.service.FilterWebService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("filter")
public class FilterController {

    private FilterWebService service;

    public FilterController(FilterWebService service) {
        this.service = service;
    }

    @PostMapping("indicator/tender")
    public List<Integer> getAvailableIndicatorsForTenderPrioritization(@RequestBody TenderPrioritizationRequest request) {
        return service.getAvailableIndicatorsForTenderPrioritization(request);
    }

    @PostMapping("indicator/buyer")
    public List<Integer> getAvailableIndicatorsForBuyerPrioritization(@RequestBody BuyerPrioritizationRequest request) {
        return service.getAvailableIndicatorsForBuyerPrioritization(request);
    }
}
