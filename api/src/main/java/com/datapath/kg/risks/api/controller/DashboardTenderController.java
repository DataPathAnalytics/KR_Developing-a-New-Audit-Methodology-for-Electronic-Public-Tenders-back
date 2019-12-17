package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.dto.dashboard.InfoDTO;
import com.datapath.kg.risks.api.request.DashboardTenderRequest;
import com.datapath.kg.risks.api.response.dashboard.ValueByFieldResponse;
import com.datapath.kg.risks.api.response.dashboard.tender.RiskTendersValueByMethodResponse;
import com.datapath.kg.risks.api.response.dashboard.tender.TenderTopResponse;
import com.datapath.kg.risks.api.response.dashboard.tender.TopOkgzByRiskTendersValueResponse;
import com.datapath.kg.risks.api.service.DashboardTenderWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dashboard/tender")
public class DashboardTenderController {

    @Autowired
    private DashboardTenderWebService service;

    @PostMapping("info")
    public InfoDTO getInfo(@RequestBody DashboardTenderRequest request) {
        return service.getInfo(request);
    }

    @PostMapping("risk-tenders-amount-info-by-method")
    public RiskTendersValueByMethodResponse getRiskTendersAmountInfoByMethod(@RequestBody DashboardTenderRequest request) {
        return service.getRiskTendersAmountInfoByMethod(request);
    }

    @PostMapping("risk-tenders-count-info-by-method")
    public RiskTendersValueByMethodResponse getRiskTendersCountInfoByMethod(@RequestBody DashboardTenderRequest request) {
        return service.getRiskTendersCountInfoByMethod(request);
    }

    @PostMapping("top-risk-tenders-by-amount")
    public TenderTopResponse getTopRiskTendersByAmount(@RequestBody DashboardTenderRequest request) {
        return service.getTopRiskTendersByAmount(request);
    }

    @PostMapping("top-tenders-by-indicator-count")
    public TenderTopResponse getTopTendersByIndicatorCount(@RequestBody DashboardTenderRequest request) {
        return service.getTopTendersByIndicatorCount(request);
    }

    @PostMapping("method-indicator-count")
    public ValueByFieldResponse getMethodIndicatorCount(@RequestBody DashboardTenderRequest request) {
        return service.getMethodIndicatorCount(request);
    }

    @PostMapping("method-indicator-amount")
    public ValueByFieldResponse getMethodIndicatorAmount(@RequestBody DashboardTenderRequest request) {
        return service.getMethodIndicatorAmount(request);
    }

    @PostMapping("top-okgz-by-risk-tenders-count")
    public TopOkgzByRiskTendersValueResponse getTopOkgzByRiskTendersCount(@RequestBody DashboardTenderRequest request) {
        return service.getTopOkgzByRiskTendersCount(request);
    }

    @PostMapping("top-okgz-by-risk-tenders-amount")
    public TopOkgzByRiskTendersValueResponse getTopOkgzByRiskTendersAmount(@RequestBody DashboardTenderRequest request) {
        return service.getTopOkgzByRiskTendersAmount(request);
    }
//
//    @PostMapping("tenders-count-info-by-month")
//    public ByMonthResponse getTendersCountInfoByMonth(@RequestBody DashboardTenderRequest request) {
//        return service.getTendersCountInfoByMonth(request);
//    }
//
//    @PostMapping("tenders-amount-info-by-month")
//    public ByMonthResponse getTendersAmountInfoByMonth(@RequestBody DashboardTenderRequest request) {
//        return service.getTendersAmountInfoByMonth(request);
//    }
}
