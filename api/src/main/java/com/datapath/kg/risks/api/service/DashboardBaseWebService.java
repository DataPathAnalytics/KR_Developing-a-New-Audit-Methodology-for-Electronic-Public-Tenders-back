package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.dto.dashboard.InfoDTO;
import com.datapath.kg.risks.api.dto.dashboard.base.TopInfoDTO;
import com.datapath.kg.risks.api.request.DashboardBaseRequest;
import com.datapath.kg.risks.api.response.dashboard.ByMonthResponse;
import com.datapath.kg.risks.api.response.dashboard.ValueByFieldInPercentResponse;
import com.datapath.kg.risks.api.response.dashboard.ValueByFieldResponse;
import com.datapath.kg.risks.api.response.dashboard.base.BaseTopResponse;

public interface DashboardBaseWebService {

    InfoDTO getInfo(DashboardBaseRequest request);

    BaseTopResponse getTopRegionsByRiskTendersCount(DashboardBaseRequest request);

    BaseTopResponse getTopMethodsByRiskTendersCount(DashboardBaseRequest request);

    BaseTopResponse getTopOkgzByRiskTendersCount(DashboardBaseRequest request);

    BaseTopResponse getTopRegionsByAmount(DashboardBaseRequest request);

    BaseTopResponse getTopMethodsByAmount(DashboardBaseRequest request);

    BaseTopResponse getTopOkgzByAmount(DashboardBaseRequest request);

    ValueByFieldResponse getRegionIndicatorCount(DashboardBaseRequest request);

    ValueByFieldResponse getRegionIndicatorAmount(DashboardBaseRequest request);

    ByMonthResponse getCountByMonth(DashboardBaseRequest request);

    ByMonthResponse getAmountByMonth(DashboardBaseRequest request);

    TopInfoDTO getTopInfo(DashboardBaseRequest request);

    ValueByFieldInPercentResponse getRegionIndicatorCountPercent(DashboardBaseRequest request);

    ValueByFieldInPercentResponse getRegionIndicatorAmountPercent(DashboardBaseRequest request);
}
