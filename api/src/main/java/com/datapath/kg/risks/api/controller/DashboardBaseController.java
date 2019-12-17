package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.dto.dashboard.InfoDTO;
import com.datapath.kg.risks.api.dto.dashboard.base.TopInfoDTO;
import com.datapath.kg.risks.api.request.DashboardBaseRequest;
import com.datapath.kg.risks.api.response.dashboard.ByMonthResponse;
import com.datapath.kg.risks.api.response.dashboard.ValueByFieldInPercentResponse;
import com.datapath.kg.risks.api.response.dashboard.ValueByFieldResponse;
import com.datapath.kg.risks.api.response.dashboard.base.BaseTopResponse;
import com.datapath.kg.risks.api.service.DashboardBaseWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dashboard/base")
public class DashboardBaseController {

    @Autowired
    private DashboardBaseWebService service;

    @PostMapping("info")
    public InfoDTO getInfo(@RequestBody DashboardBaseRequest request) {
        return service.getInfo(request);
    }

    @PostMapping("top-regions-by-risk-tenders-count")
    public BaseTopResponse getTopRegionsByRiskTendersCount(@RequestBody DashboardBaseRequest request) {
        return service.getTopRegionsByRiskTendersCount(request);
    }

    @PostMapping("top-methods-by-risk-tenders-count")
    public BaseTopResponse getTopMethodsByRiskTendersCount(@RequestBody DashboardBaseRequest request) {
        return service.getTopMethodsByRiskTendersCount(request);
    }

    @PostMapping("top-okgz-by-risk-tenders-count")
    public BaseTopResponse getTopOkgzByRiskTendersCount(@RequestBody DashboardBaseRequest request) {
        return service.getTopOkgzByRiskTendersCount(request);
    }

    @PostMapping("top-regions-by-risk-tenders-amount")
    public BaseTopResponse getTopRegionsByAmount(@RequestBody DashboardBaseRequest request) {
        return service.getTopRegionsByAmount(request);
    }

    @PostMapping("top-methods-by-risk-tenders-amount")
    public BaseTopResponse getTopMethodsByAmount(@RequestBody DashboardBaseRequest request) {
        return service.getTopMethodsByAmount(request);
    }

    @PostMapping("top-okgz-by-risk-tenders-amount")
    public BaseTopResponse getTopOkgzByAmount(@RequestBody DashboardBaseRequest request) {
        return service.getTopOkgzByAmount(request);
    }

    @PostMapping("region-indicator-count")
    public ValueByFieldResponse getRegionIndicatorCount(@RequestBody DashboardBaseRequest request) {
        return service.getRegionIndicatorCount(request);
    }

    @PostMapping("region-indicator-amount")
    public ValueByFieldResponse getRegionIndicatorAmount(@RequestBody DashboardBaseRequest request) {
        return service.getRegionIndicatorAmount(request);
    }

    @PostMapping("region-indicator-count-percent")
    public ValueByFieldInPercentResponse getRegionIndicatorCountPercent(@RequestBody DashboardBaseRequest request) {
        return service.getRegionIndicatorCountPercent(request);
    }

    @PostMapping("region-indicator-amount-percent")
    public ValueByFieldInPercentResponse getRegionIndicatorAmountPercent(@RequestBody DashboardBaseRequest request) {
        return service.getRegionIndicatorAmountPercent(request);
    }

    @PostMapping("count-by-month")
    public ByMonthResponse getCountByMonth(@RequestBody DashboardBaseRequest request) {
        return service.getCountByMonth(request);
    }

    @PostMapping("amount-by-month")
    public ByMonthResponse getAmountByMonth(@RequestBody DashboardBaseRequest request) {
        return service.getAmountByMonth(request);
    }

    @PostMapping("top-info")
    public TopInfoDTO getTopInfo(@RequestBody DashboardBaseRequest request) {
        return service.getTopInfo(request);
    }
}
