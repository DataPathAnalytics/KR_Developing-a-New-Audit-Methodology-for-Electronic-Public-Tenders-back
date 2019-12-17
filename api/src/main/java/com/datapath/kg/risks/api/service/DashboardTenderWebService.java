package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.dto.dashboard.InfoDTO;
import com.datapath.kg.risks.api.request.DashboardTenderRequest;
import com.datapath.kg.risks.api.response.dashboard.ByMonthResponse;
import com.datapath.kg.risks.api.response.dashboard.ValueByFieldResponse;
import com.datapath.kg.risks.api.response.dashboard.tender.RiskTendersValueByMethodResponse;
import com.datapath.kg.risks.api.response.dashboard.tender.TenderTopResponse;
import com.datapath.kg.risks.api.response.dashboard.tender.TopOkgzByRiskTendersValueResponse;

public interface DashboardTenderWebService {
    InfoDTO getInfo(DashboardTenderRequest request);

    TenderTopResponse getTopRiskTendersByAmount(DashboardTenderRequest request);

    TenderTopResponse getTopTendersByIndicatorCount(DashboardTenderRequest request);

    ValueByFieldResponse getMethodIndicatorCount(DashboardTenderRequest request);

    ByMonthResponse getTendersCountInfoByMonth(DashboardTenderRequest request);

    RiskTendersValueByMethodResponse getRiskTendersAmountInfoByMethod(DashboardTenderRequest request);

    ValueByFieldResponse getMethodIndicatorAmount(DashboardTenderRequest request);

    ByMonthResponse getTendersAmountInfoByMonth(DashboardTenderRequest request);

    RiskTendersValueByMethodResponse getRiskTendersCountInfoByMethod(DashboardTenderRequest request);

    TopOkgzByRiskTendersValueResponse getTopOkgzByRiskTendersCount(DashboardTenderRequest request);

    TopOkgzByRiskTendersValueResponse getTopOkgzByRiskTendersAmount(DashboardTenderRequest request);
}
