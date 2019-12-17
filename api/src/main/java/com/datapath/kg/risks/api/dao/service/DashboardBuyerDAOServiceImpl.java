package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.model.dashboard.PrioritizationInfoModel;
import com.datapath.kg.risks.api.dao.model.dashboard.buyer.BuyerTopModel;
import com.datapath.kg.risks.api.request.DashboardBuyerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class DashboardBuyerDAOServiceImpl implements DashboardBuyerDAOService {

    @Autowired
    private JdbcTemplate template;

    private final String PRIORITIZATION_INFO_QUERY = "SELECT count(t.id)         tenders_count,\n" +
            "       COALESCE(SUM(t.completed_lot_value), 0)                                              tenders_amount,\n" +
            "       count(t.id) FILTER (WHERE t.passed_indicators_count > 0)                risk_tenders_count,\n" +
            "       COALESCE(SUM(t.completed_lot_value) FILTER (WHERE t.passed_indicators_count > 0), 0)   risk_tenders_amount,\n" +
            "       count(DISTINCT t.buyer_id) buyers_count,\n" +
            "       count(DISTINCT t.buyer_id) FILTER (WHERE t.passed_indicators_count > 0) risk_buyers_count\n" +
            "FROM tender_prioritization t WHERE TRUE %s";

    private final String TOP_BY_RISK_TENDERS_COUNT_QUERY = "SELECT t.buyer_id,\n" +
            "       t.identifier_legal_name_ru                               as name,\n" +
            "       count(t.id) FILTER (WHERE t.passed_indicators_count > 0) as risk_value,\n" +
            "       count(t.id) FILTER (WHERE t.passed_indicators_count = 0) as value\n" +
            "FROM tender_prioritization t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY t.buyer_id, t.identifier_legal_name_ru\n" +
            "ORDER BY risk_value DESC\n" +
            "LIMIT 10";

    private final String TOP_BY_RISK_TENDERS_AMOUNT_QUERY = "SELECT t.buyer_id,\n" +
            "       t.identifier_legal_name_ru                                                           as name,\n" +
            "       COALESCE(SUM(t.completed_lot_value) FILTER (WHERE t.passed_indicators_count > 0), 0) as risk_value,\n" +
            "       COALESCE(SUM(t.completed_lot_value) FILTER (WHERE t.passed_indicators_count = 0), 0) as value\n" +
            "FROM tender_prioritization t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY t.buyer_id, t.identifier_legal_name_ru\n" +
            "ORDER BY risk_value DESC\n" +
            "LIMIT 10";

    private final String TOP_BY_INDICATORS_COUNT_QUERY = "WITH tender_indicator AS (SELECT buyer_id,\n" +
            "                                 identifier_legal_name_ru,\n" +
            "                                 unnest(passed_indicator_list) AS indicator_id,\n" +
            "                                 procurement_method_details,\n" +
            "                                 region,\n" +
            "                                 risk_level,\n" +
            "                                 cpv_list,\n" +
            "                                 contract_date\n" +
            "                          FROM tender_prioritization)\n" +
            "SELECT t.buyer_id, t.identifier_legal_name_ru as name, count(DISTINCT t.indicator_id) AS value\n" +
            "FROM tender_indicator t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY t.buyer_id, t.identifier_legal_name_ru\n" +
            "ORDER BY value DESC, t.identifier_legal_name_ru\n" +
            "LIMIT 10";

    @Override
    public PrioritizationInfoModel getPrioritizationInfo(DashboardBuyerRequest request) {
        return template.queryForObject(String.format(
                PRIORITIZATION_INFO_QUERY,
                getFiltersForInfo(request)), new BeanPropertyRowMapper<>(PrioritizationInfoModel.class));
    }

    @Override
    public List<BuyerTopModel> getTopByRiskTendersCount(DashboardBuyerRequest request) {
        return template.query(String.format(
                TOP_BY_RISK_TENDERS_COUNT_QUERY,
                getFiltersForInfo(request)), new BeanPropertyRowMapper<>(BuyerTopModel.class));
    }

    @Override
    public List<BuyerTopModel> getTopByRiskTendersAmount(DashboardBuyerRequest request) {
        return template.query(String.format(
                TOP_BY_RISK_TENDERS_AMOUNT_QUERY,
                getFiltersForInfo(request)), new BeanPropertyRowMapper<>(BuyerTopModel.class));
    }

    @Override
    public List<BuyerTopModel> getTopByIndicatorsCount(DashboardBuyerRequest request) {
        return template.query(String.format(
                TOP_BY_INDICATORS_COUNT_QUERY,
                getFiltersForTopByIndicatorCount(request)), new BeanPropertyRowMapper<>(BuyerTopModel.class));
    }

    private String getFiltersForTopByIndicatorCount(DashboardBuyerRequest request) {
        StringBuilder filters = new StringBuilder();
        filters.append(getPeriodFilter(request));
        filters.append(getMethodsFilter(request));
        filters.append(getBuyerIdsFilter(request));
        filters.append(getIndicatorFilter(request));
        filters.append(getOkgzFilter(request));
        filters.append(getRiskLevelFilter(request));
        filters.append(getRegionsFilter(request));
        return filters.toString();
    }

    private String getIndicatorFilter(DashboardBuyerRequest request) {
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

    private String getFiltersForInfo(DashboardBuyerRequest request) {
        StringBuilder filters = new StringBuilder();
        filters.append(getPeriodFilter(request));
        filters.append(getMethodsFilter(request));
        filters.append(getBuyerIdsFilter(request));
        filters.append(getIndicatorsFilter(request));
        filters.append(getOkgzFilter(request));
        filters.append(getRiskLevelFilter(request));
        filters.append(getRegionsFilter(request));
        return filters.toString();
    }

    private String getPeriodFilter(DashboardBuyerRequest request) {
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

    private String getMethodsFilter(DashboardBuyerRequest request) {
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

    private String getBuyerIdsFilter(DashboardBuyerRequest request) {
        StringBuilder filter = new StringBuilder();
        if (!isEmpty(request.getBuyerIds())) {
            String buyersIds = request.getBuyerIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filter.append(" AND t.buyer_id IN (")
                    .append(buyersIds)
                    .append(")");
        }
        return filter.toString();
    }

    private String getIndicatorsFilter(DashboardBuyerRequest request) {
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

    private String getOkgzFilter(DashboardBuyerRequest request) {
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

    private String getRiskLevelFilter(DashboardBuyerRequest request) {
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

    private String getRegionsFilter(DashboardBuyerRequest request) {
        StringBuilder regionsFilter = new StringBuilder();
        if (!isEmpty(request.getRegions())) {
            String regions = request.getRegions()
                    .stream()
                    .map(method -> "'" + method + "'")
                    .collect(Collectors.joining(", "));

            regionsFilter
                    .append(" AND t.region IN (")
                    .append(regions)
                    .append(")");
        }
        return regionsFilter.toString();
    }
}
