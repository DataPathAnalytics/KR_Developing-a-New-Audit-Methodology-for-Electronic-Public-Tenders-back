package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.model.dashboard.PrioritizationInfoModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByFieldModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByMonthModel;
import com.datapath.kg.risks.api.dao.model.dashboard.tender.RiskTendersValueByMethodModel;
import com.datapath.kg.risks.api.dao.model.dashboard.tender.TenderTopModel;
import com.datapath.kg.risks.api.dao.model.dashboard.tender.TopOkgzByRiskTendersValueModel;
import com.datapath.kg.risks.api.request.DashboardTenderRequest;

import java.util.List;

public interface DashboardTenderDAOService {

    PrioritizationInfoModel getPrioritizationInfo(DashboardTenderRequest request);

    List<TenderTopModel> getTopRiskTendersByAmount(DashboardTenderRequest request);

    List<TenderTopModel> getTopTendersByIndicatorCount(DashboardTenderRequest request);

    List<ValueByFieldModel> getTendersCountByMethodAndIndicator(DashboardTenderRequest request);

    List<ValueByMonthModel> getTendersCountInfoByMonth(DashboardTenderRequest request);

    List<RiskTendersValueByMethodModel> getRiskTendersAmountInfoByMethod(DashboardTenderRequest request);

    List<ValueByFieldModel> getTendersAmountByMethodAndIndicator(DashboardTenderRequest request);

    List<ValueByMonthModel> getTendersAmountInfoByMonth(DashboardTenderRequest request);

    List<RiskTendersValueByMethodModel> getRiskTendersCountInfoByMethod(DashboardTenderRequest request);

    List<TopOkgzByRiskTendersValueModel> getTopOkgzByRiskTendersCount(DashboardTenderRequest request);

    List<TopOkgzByRiskTendersValueModel> getTopOkgzByRiskTendersAmount(DashboardTenderRequest request);
}
