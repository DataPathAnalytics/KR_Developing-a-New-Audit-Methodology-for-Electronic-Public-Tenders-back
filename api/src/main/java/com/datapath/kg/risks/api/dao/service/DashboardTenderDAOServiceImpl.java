package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.model.dashboard.PrioritizationInfoModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByFieldModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByMonthModel;
import com.datapath.kg.risks.api.dao.model.dashboard.tender.RiskTendersValueByMethodModel;
import com.datapath.kg.risks.api.dao.model.dashboard.tender.TenderTopModel;
import com.datapath.kg.risks.api.dao.model.dashboard.tender.TopOkgzByRiskTendersValueModel;
import com.datapath.kg.risks.api.request.DashboardTenderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.datapath.kg.risks.api.Constants.WITHOUT_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE;
import static com.datapath.kg.risks.api.Constants.WITH_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class DashboardTenderDAOServiceImpl implements DashboardTenderDAOService {

    @Autowired
    private JdbcTemplate template;

    private final String PRIORITIZATION_INFO_REQUEST = "SELECT count(t.id)         tenders_count,\n" +
            "       COALESCE(SUM(t.completed_lot_value), 0)                                              tenders_amount,\n" +
            "       count(t.id) FILTER (WHERE t.passed_indicators_count > 0)                risk_tenders_count,\n" +
            "       COALESCE(SUM(t.completed_lot_value) FILTER (WHERE t.passed_indicators_count > 0), 0)   risk_tenders_amount\n" +
            "FROM tender_prioritization t WHERE TRUE %s";

    private final String TOP_RISK_TENDERS_BY_AMOUNT_REQUEST = "SELECT t.id                  AS tender_id,\n" +
            "       t.completed_lot_value AS result,\n" +
            "       CASE\n" +
            "         WHEN array_length(t.cpv_list, 1) = 1 THEN t.cpv_list[1]\n" +
            "         ELSE null END       as code,\n" +
            "       t.date_published\n" +
            "FROM tender_prioritization t\n" +
            "WHERE t.passed_indicators_count > 0 %s\n" +
            "ORDER BY result DESC, code\n" +
            "LIMIT 10";

    private final String TOP_TENDERS_BY_INDICATOR_COUNT_REQUEST = "SELECT t.id                      AS tender_id,\n" +
            "       t.passed_indicators_count AS result,\n" +
            "       CASE\n" +
            "         WHEN array_length(t.cpv_list, 1) = 1 THEN t.cpv_list[1]\n" +
            "         ELSE null END           as code,\n" +
            "       t.date_published\n" +
            "FROM tender_prioritization t\n" +
            "WHERE TRUE %s\n" +
            "ORDER BY result DESC, code\n" +
            "LIMIT 10";

    private final String TENDERS_COUNT_BY_METHOD_AND_INDICATOR_REQUEST = "WITH tender_method_indicator AS (SELECT id,\n" +
            "                                        procurement_method_details,\n" +
            "                                        unnest(passed_indicator_list) indicator_id,\n" +
            "                                        completed_lot_value,\n" +
            "                                        cpv_list,\n" +
            "                                        contract_date,\n" +
            "                                        risk_level,\n" +
            "                                        buyer_id\n" +
            "                                 FROM tender_prioritization)\n" +
            "SELECT t.procurement_method_details description, t.indicator_id, COUNT(t.id) AS value\n" +
            "FROM tender_method_indicator t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY description, t.indicator_id";

    private final String TENDERS_AMOUNT_BY_METHOD_AND_INDICATOR_REQUEST = "WITH tender_method_indicator AS (SELECT id,\n" +
            "                                        procurement_method_details,\n" +
            "                                        unnest(passed_indicator_list) indicator_id,\n" +
            "                                        completed_lot_value,\n" +
            "                                        cpv_list,\n" +
            "                                        contract_date,\n" +
            "                                        risk_level,\n" +
            "                                        buyer_id\n" +
            "                                 FROM tender_prioritization)\n" +
            "SELECT t.procurement_method_details description, t.indicator_id, COALESCE(SUM(t.completed_lot_value), 0) AS value\n" +
            "FROM tender_method_indicator t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY description, t.indicator_id";

    private final String TENDERS_COUNT_INFO_BY_MONTH_REQUEST = "SELECT CONCAT(EXTRACT(YEAR FROM t.contract_date), '-', EXTRACT(MONTH FROM t.contract_date)) AS date,\n" +
            "       COUNT(t.id)                                                                          AS value,\n" +
            "       COUNT(t.id) FILTER (WHERE t.passed_indicators_count > 0)                             AS value_with_risk\n" +
            "FROM tender_prioritization t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY date";

    private final String TENDERS_AMOUNT_INFO_BY_MONTH_REQUEST = "SELECT CONCAT(EXTRACT(YEAR FROM t.contract_date), '-', EXTRACT(MONTH FROM t.contract_date)) AS date,\n" +
            "       COALESCE(SUM(t.completed_lot_value))                                                 AS value,\n" +
            "       COALESCE(SUM(t.completed_lot_value) FILTER (WHERE t.passed_indicators_count > 0), 0) AS value_with_risk\n" +
            "FROM tender_prioritization t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY date";

    private final String RISK_TENDERS_AMOUNT_INFO_BY_METHOD_REQUEST = "SELECT t.procurement_method_details AS method, coalesce(sum(t.completed_lot_value), 0) AS value\n" +
            "FROM tender_prioritization t\n" +
            "WHERE t.passed_indicators_count > 0 %s\n" +
            "GROUP BY t.procurement_method_details";

    private final String RISK_TENDERS_COUNT_INFO_BY_METHOD_REQUEST = "SELECT t.procurement_method_details AS method, count(t.id) AS value\n" +
            "FROM tender_prioritization t\n" +
            "WHERE t.passed_indicators_count > 0 %s\n" +
            "GROUP BY t.procurement_method_details";

    private final String TOP_OKGZ_BY_RISK_TENDERS_COUNT_REQUEST = "WITH tender_cpv AS (SELECT id,\n" +
            "                           procurement_method_details,\n" +
            "                           unnest(cpv_list) cpv,\n" +
            "                           completed_lot_value,\n" +
            "                           passed_indicator_list,\n" +
            "                           contract_date,\n" +
            "                           risk_level,\n" +
            "                           buyer_id\n" +
            "                    FROM tender_prioritization\n" +
            "                    WHERE passed_indicators_count > 0)\n" +
            "SELECT t.cpv, COUNT(t.id) AS value\n" +
            "FROM tender_cpv t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY t.cpv\n" +
            "ORDER BY value DESC\n" +
            "LIMIT 10";

    private final String TOP_OKGZ_BY_RISK_TENDERS_AMOUNT_REQUEST = "WITH tender_cpv AS (SELECT id,\n" +
            "                           procurement_method_details,\n" +
            "                           unnest(cpv_list) cpv,\n" +
            "                           completed_lot_value,\n" +
            "                           passed_indicator_list,\n" +
            "                           contract_date,\n" +
            "                           risk_level,\n" +
            "                           buyer_id\n" +
            "                    FROM tender_prioritization\n" +
            "                    WHERE passed_indicators_count > 0)\n" +
            "SELECT t.cpv, COALESCE(SUM(t.completed_lot_value), 0) AS value\n" +
            "FROM tender_cpv t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY t.cpv\n" +
            "ORDER BY value DESC\n" +
            "LIMIT 10";

    @Override
    public PrioritizationInfoModel getPrioritizationInfo(DashboardTenderRequest request) {
        return template.queryForObject(String.format(PRIORITIZATION_INFO_REQUEST, getFiltersForBaseInfo(request)), new BeanPropertyRowMapper<>(PrioritizationInfoModel.class));
    }

    @Override
    public List<TenderTopModel> getTopRiskTendersByAmount(DashboardTenderRequest request) {
        return template.query(String.format(TOP_RISK_TENDERS_BY_AMOUNT_REQUEST, getFiltersForInfo(request)), new BeanPropertyRowMapper<>(TenderTopModel.class));
    }

    @Override
    public List<TenderTopModel> getTopTendersByIndicatorCount(DashboardTenderRequest request) {
        return template.query(String.format(TOP_TENDERS_BY_INDICATOR_COUNT_REQUEST, getFiltersForInfo(request)), new BeanPropertyRowMapper<>(TenderTopModel.class));
    }

    @Override
    public List<ValueByFieldModel> getTendersCountByMethodAndIndicator(DashboardTenderRequest request) {
        return template.query(
                String.format(TENDERS_COUNT_BY_METHOD_AND_INDICATOR_REQUEST,
                        getFiltersForTenderValueByMethodAndIndicator(request)),
                new BeanPropertyRowMapper<>(ValueByFieldModel.class));
    }

    @Override
    public List<ValueByMonthModel> getTendersCountInfoByMonth(DashboardTenderRequest request) {
        return template.query(
                String.format(TENDERS_COUNT_INFO_BY_MONTH_REQUEST,
                        getFiltersForTenderValueByMonth(request)),
                new BeanPropertyRowMapper<>(ValueByMonthModel.class));
    }

    @Override
    public List<RiskTendersValueByMethodModel> getRiskTendersAmountInfoByMethod(DashboardTenderRequest request) {
        return template.query(String.format(RISK_TENDERS_AMOUNT_INFO_BY_METHOD_REQUEST, getFiltersForInfo(request)), new BeanPropertyRowMapper<>(RiskTendersValueByMethodModel.class));
    }

    @Override
    public List<RiskTendersValueByMethodModel> getRiskTendersCountInfoByMethod(DashboardTenderRequest request) {
        return template.query(String.format(RISK_TENDERS_COUNT_INFO_BY_METHOD_REQUEST, getFiltersForInfo(request)), new BeanPropertyRowMapper<>(RiskTendersValueByMethodModel.class));
    }

    @Override
    public List<ValueByFieldModel> getTendersAmountByMethodAndIndicator(DashboardTenderRequest request) {
        return template.query(
                String.format(TENDERS_AMOUNT_BY_METHOD_AND_INDICATOR_REQUEST,
                        getFiltersForTenderValueByMethodAndIndicator(request)),
                new BeanPropertyRowMapper<>(ValueByFieldModel.class));
    }

    @Override
    public List<ValueByMonthModel> getTendersAmountInfoByMonth(DashboardTenderRequest request) {
        return template.query(
                String.format(TENDERS_AMOUNT_INFO_BY_MONTH_REQUEST,
                        getFiltersForTenderValueByMonth(request)),
                new BeanPropertyRowMapper<>(ValueByMonthModel.class));
    }

    @Override
    public List<TopOkgzByRiskTendersValueModel> getTopOkgzByRiskTendersCount(DashboardTenderRequest request) {
        return template.query(String.format(TOP_OKGZ_BY_RISK_TENDERS_COUNT_REQUEST, getFiltersForTopOkgz(request)), new BeanPropertyRowMapper<>(TopOkgzByRiskTendersValueModel.class));
    }

    @Override
    public List<TopOkgzByRiskTendersValueModel> getTopOkgzByRiskTendersAmount(DashboardTenderRequest request) {
        return template.query(String.format(TOP_OKGZ_BY_RISK_TENDERS_AMOUNT_REQUEST, getFiltersForTopOkgz(request)), new BeanPropertyRowMapper<>(TopOkgzByRiskTendersValueModel.class));
    }

    private String getFiltersForTopOkgz(DashboardTenderRequest request) {
        StringBuilder filters = new StringBuilder();
        filters.append(getPeriodFilter(request));
        filters.append(getMethodsFilter(request));
        filters.append(getBuyerIdFilter(request));
        filters.append(getIndicatorsFilter(request));
        filters.append(getOkgzOneFilter(request));
        filters.append(getCompletedLotValueFilter(request));
        filters.append(getTenderIdFilter(request));
        filters.append(getRiskLevelFilter(request));
        filters.append(getChecklistInfoFilter(request));
        return filters.toString();
    }

    private String getFiltersForTenderValueByMonth(DashboardTenderRequest request) {
        StringBuilder filters = new StringBuilder();
        filters.append(getPeriodByMonthFilter(request));
        filters.append(getMethodsFilter(request));
        filters.append(getBuyerIdFilter(request));
        filters.append(getIndicatorsFilter(request));
        filters.append(getOkgzFilter(request));
        filters.append(getCompletedLotValueFilter(request));
        filters.append(getTenderIdFilter(request));
        filters.append(getRiskLevelFilter(request));
        filters.append(getChecklistInfoFilter(request));
        return filters.toString();
    }

    private String getFiltersForTenderValueByMethodAndIndicator(DashboardTenderRequest request) {
        StringBuilder filters = new StringBuilder();
        filters.append(getPeriodFilter(request));
        filters.append(getMethodsFilter(request));
        filters.append(getBuyerIdFilter(request));
        filters.append(getIndicatorFilter(request));
        filters.append(getOkgzFilter(request));
        filters.append(getCompletedLotValueFilter(request));
        filters.append(getTenderIdFilter(request));
        filters.append(getRiskLevelFilter(request));
        filters.append(getChecklistInfoFilter(request));
        return filters.toString();
    }

    private String getFiltersForBaseInfo(DashboardTenderRequest request) {
        StringBuilder filters = new StringBuilder();
        filters.append(getPeriodFilter(request));
        filters.append(getMethodsFilter(request));
        filters.append(getBuyerIdFilter(request));
        filters.append(getIndicatorsFilterWithIndicatorStatus(request));
        filters.append(getOkgzFilter(request));
        filters.append(getCompletedLotValueFilter(request));
        filters.append(getTenderIdFilter(request));
        filters.append(getRiskLevelFilter(request));
        filters.append(getChecklistInfoFilter(request));
        return filters.toString();
    }

    private String getIndicatorsFilterWithIndicatorStatus(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (isNull(request.getIndicatorStatus())) {
            if (!isEmpty(request.getIndicators())) {
                String indicators = request.getIndicators()
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));

                filter
                        .append(" AND t.passed_indicator_list && '{")
                        .append(indicators)
                        .append("}'");
            }
        } else {
            if (request.getIndicatorStatus().equals(1)) {
                filter.append(" AND t.passed_indicators_count > 0");
            } else {
                filter.append(" AND t.passed_indicators_count = 0");
            }
        }
        return filter.toString();
    }

    private String getFiltersForInfo(DashboardTenderRequest request) {
        StringBuilder filters = new StringBuilder();
        filters.append(getPeriodFilter(request));
        filters.append(getMethodsFilter(request));
        filters.append(getBuyerIdFilter(request));
        filters.append(getIndicatorsFilter(request));
        filters.append(getOkgzFilter(request));
        filters.append(getCompletedLotValueFilter(request));
        filters.append(getTenderIdFilter(request));
        filters.append(getRiskLevelFilter(request));
        filters.append(getChecklistInfoFilter(request));
        return filters.toString();
    }

    private String getPeriodByMonthFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (nonNull(request.getContractStartDate()) || nonNull(request.getContractEndDate())) {
            if (nonNull(request.getContractStartDate())) {
                filter
                        .append(" AND t.contract_date >= '")
                        .append(request.getContractStartDate())
                        .append("'");
            }
            if (nonNull(request.getContractEndDate())) {
                filter
                        .append(" AND t.contract_date <= '")
                        .append(request.getContractEndDate())
                        .append("'");
            }
        } else {
            filter.append(" AND t.contract_date >= now() - INTERVAL '1 year'");
        }
        return filter.toString();
    }

    private String getIndicatorFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (!isEmpty(request.getIndicators())) {
            String indicators = request.getIndicators()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filter
                    .append(" AND t.indicator_id IN (")
                    .append(indicators)
                    .append(")");
        }
        return filter.toString();
    }

    private String getRiskLevelFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (!isEmpty(request.getRiskLevel())) {
            String riskLevels = request.getRiskLevel()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filter
                    .append(" AND t.risk_level IN (")
                    .append(riskLevels)
                    .append(")");
        }
        return filter.toString();
    }

    private String getTenderIdFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (nonNull(request.getTenderId())) {
            filter
                    .append(" AND t.id = ")
                    .append(request.getTenderId());
        }
        return filter.toString();
    }

    private String getCompletedLotValueFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (nonNull(request.getCompletedLotValueMin())) {
            filter
                    .append(" AND t.completed_lot_value >= ")
                    .append(request.getCompletedLotValueMin());
        }

        if (nonNull(request.getCompletedLotValueMax())) filter
                .append(" AND t.completed_lot_value <= ")
                .append(request.getCompletedLotValueMax());
        return filter.toString();
    }

    private String getOkgzFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (!isEmpty(request.getCpv())) {
            String cpv = request.getCpv()
                    .stream()
                    .map(c -> "\"" + c + "\"")
                    .collect(Collectors.joining(", "));

            filter
                    .append(" AND t.cpv_list && '{")
                    .append(cpv)
                    .append("}'");
        }
        return filter.toString();
    }

    private String getOkgzOneFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (!isEmpty(request.getCpv())) {
            String cpv = request.getCpv()
                    .stream()
                    .map(c -> "'" + c + "'")
                    .collect(Collectors.joining(", "));

            filter
                    .append(" AND t.cpv IN (")
                    .append(cpv)
                    .append(")");
        }
        return filter.toString();
    }

    private String getIndicatorsFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (!isEmpty(request.getIndicators())) {
            String indicators = request.getIndicators()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filter
                    .append(" AND t.passed_indicator_list && '{")
                    .append(indicators)
                    .append("}'");
        }
        return filter.toString();
    }

    private String getBuyerIdFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (!isEmpty(request.getBuyerIds())) {
            String buyerIds = request.getBuyerIds()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));
            filter
                    .append(" AND t.buyer_id IN (")
                    .append(buyerIds)
                    .append(")");
        }
        return filter.toString();
    }

    private String getMethodsFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (!isEmpty(request.getProcurementMethodDetails())) {
            String procurementMethodDetails = request.getProcurementMethodDetails()
                    .stream()
                    .map(method -> "'" + method + "'")
                    .collect(Collectors.joining(", "));

            filter
                    .append(" AND t.procurement_method_details IN (")
                    .append(procurementMethodDetails)
                    .append(")");
        }
        return filter.toString();
    }

    private String getPeriodFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (nonNull(request.getContractStartDate()) || nonNull(request.getContractEndDate())) {
            if (nonNull(request.getContractStartDate())) {
                filter
                        .append(" AND t.contract_date >= '")
                        .append(request.getContractStartDate())
                        .append("'");
            }
            if (nonNull(request.getContractEndDate())) {
                filter
                        .append(" AND t.contract_date <= '")
                        .append(request.getContractEndDate())
                        .append("'");
            }
        } else {
            filter.append(" AND EXTRACT(YEARS FROM t.contract_date) = EXTRACT(YEARS FROM now())");
        }
        return filter.toString();
    }

    private String getChecklistInfoFilter(DashboardTenderRequest request) {
        StringBuilder filter = new StringBuilder();
        if (WITHOUT_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE.equals(request.getChecklistType())) {
            filter.append(" AND t.id NOT IN (SELECT c.tender_id FROM checklist c WHERE c.tender_id IS NOT NULL)");
        } else if (WITH_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE.equals(request.getChecklistType()) ||
                !isEmpty(request.getAuditorIds()) ||
                nonNull(request.getChecklistStatus())) {

            filter.append(" AND t.id IN (SELECT c.tender_id FROM checklist c WHERE c.tender_id IS NOT NULL");

            if (!isEmpty(request.getAuditorIds())) {
                String auditorIds = request.getAuditorIds()
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));

                filter
                        .append(" AND c.auditor_id IN (")
                        .append(auditorIds)
                        .append(")");
            }
            if (nonNull(request.getChecklistStatus())) {
                filter
                        .append(" AND c.status_id = ")
                        .append(request.getChecklistStatus());
            }
            filter.append(")");
        }

        return filter.toString();
    }

}
