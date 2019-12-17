package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.dto.dashboard.InfoDTO;
import com.datapath.kg.risks.api.request.DashboardBuyerRequest;
import com.datapath.kg.risks.api.response.dashboard.buyer.BuyerTopByIndicatorCountResponse;
import com.datapath.kg.risks.api.response.dashboard.buyer.BuyerTopResponse;
import com.datapath.kg.risks.api.service.DashboardBuyerWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dashboard/buyer")
public class DashboardBuyerController {

    @Autowired
    private DashboardBuyerWebService service;

    @PostMapping("info")
    public InfoDTO getInfo(@RequestBody DashboardBuyerRequest request) {
        return service.getInfo(request);
    }

    @PostMapping("top-by-risk-tenders-count")
    public BuyerTopResponse getTopByRiskTendersCount(@RequestBody DashboardBuyerRequest request) {
        return service.getTopByRiskTendersCount(request);
    }

    @PostMapping("top-by-risk-tenders-amount")
    public BuyerTopResponse getTopByRiskTendersAmount(@RequestBody DashboardBuyerRequest request) {
        return service.getTopByRiskTendersAmount(request);
    }

    @PostMapping("top-by-indicators-count")
    public BuyerTopByIndicatorCountResponse getTopByIndicatorsCount(@RequestBody DashboardBuyerRequest request) {
        return service.getTopByIndicatorsCount(request);
    }
}
