package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.model.dashboard.PrioritizationInfoModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByFieldModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByMonthModel;
import com.datapath.kg.risks.api.dao.model.dashboard.base.BaseTopModel;
import com.datapath.kg.risks.api.request.DashboardBaseRequest;

import java.util.List;

public interface DashboardBaseDAOService {

    PrioritizationInfoModel getPrioritizationInfo(DashboardBaseRequest request);

    List<BaseTopModel> getTopRegionsByRiskTendersCount(DashboardBaseRequest request);

    List<BaseTopModel> getTopMethodsByRiskTendersCount(DashboardBaseRequest request);

    List<BaseTopModel> getTopOkgzByRiskTendersCount(DashboardBaseRequest request);

    List<BaseTopModel> getTopRegionsByAmount(DashboardBaseRequest request);

    List<BaseTopModel> getTopMethodsByAmount(DashboardBaseRequest request);

    List<BaseTopModel> getTopOkgzByAmount(DashboardBaseRequest request);

    List<ValueByFieldModel> getTendersCountByRegionAndIndicator(DashboardBaseRequest request);

    List<ValueByFieldModel> getTendersAmountByRegionAndIndicator(DashboardBaseRequest request);

    List<ValueByMonthModel> getTendersCountByMonth(DashboardBaseRequest request);

    List<ValueByMonthModel> getTendersAmountByMonth(DashboardBaseRequest request);

    Long getTopTender(DashboardBaseRequest request);

    String getTopBuyer(DashboardBaseRequest request);

    Integer getTopIndicator(DashboardBaseRequest request);
}
