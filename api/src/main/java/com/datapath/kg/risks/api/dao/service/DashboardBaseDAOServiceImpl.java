package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.model.dashboard.PrioritizationInfoModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByFieldModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByMonthModel;
import com.datapath.kg.risks.api.dao.model.dashboard.base.BaseTopModel;
import com.datapath.kg.risks.api.request.DashboardBaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class DashboardBaseDAOServiceImpl implements DashboardBaseDAOService {

    @Autowired
    private JdbcTemplate template;

    private final String PRIORITIZATION_INFO_QUERY = "SELECT count(t.id)                                                                          tenders_count,\n" +
            "       COALESCE(SUM(t.completed_lot_value), 0)                                              tenders_amount,\n" +
            "       count(t.id) FILTER (WHERE t.passed_indicators_count > 0)                             risk_tenders_count,\n" +
            "       COALESCE(SUM(t.completed_lot_value) FILTER (WHERE t.passed_indicators_count > 0), 0) risk_tenders_amount,\n" +
            "       count(DISTINCT t.buyer_id)                                                           buyers_count,\n" +
            "       count(DISTINCT t.buyer_id) FILTER (WHERE t.passed_indicators_count > 0)              risk_buyers_count\n" +
            "FROM tender_prioritization t\n" +
            "WHERE TRUE %s";

    private final String TOP_REGIONS_BY_RISK_TENDERS_COUNT_QUERY = "SELECT region description, COUNT(t.id) result\n" +
            "FROM tender_prioritization t\n" +
            "WHERE t.passed_indicators_count > 0 %s\n" +
            "GROUP BY region\n" +
            "ORDER BY result DESC\n" +
            "LIMIT 5";

    private final String TOP_METHODS_BY_RISK_TENDERS_COUNT_QUERY = "SELECT t.procurement_method_details description, COUNT(t.id) result\n" +
            "FROM tender_prioritization t\n" +
            "WHERE t.passed_indicators_count > 0 %s\n" +
            "GROUP BY t.procurement_method_details\n" +
            "ORDER BY result DESC\n" +
            "LIMIT 5";

    private final String TOP_OKGZ_BY_RISK_TENDERS_COUNT_QUERY = "WITH tender_cpv AS (SELECT t.id,\n" +
            "                           unnest(t.cpv_list) cpv,\n" +
            "                           t.region,\n" +
            "                           t.passed_indicator_list,\n" +
            "                           t.contract_date,\n" +
            "                           t.procurement_method_details\n" +
            "                    FROM tender_prioritization t\n" +
            "                    WHERE t.passed_indicators_count > 0)\n" +
            "SELECT t.cpv description, COUNT(t.id) result\n" +
            "FROM tender_cpv t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY description\n" +
            "ORDER BY result DESC\n" +
            "LIMIT 5;";

    private final String TOP_REGIONS_BY_AMOUNT_QUERY = "SELECT region description, COALESCE(SUM(t.completed_lot_value), 0) result\n" +
            "FROM tender_prioritization t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY region\n" +
            "ORDER BY result DESC\n" +
            "LIMIT 5";

    private final String TOP_METHODS_BY_AMOUNT_QUERY = "SELECT t.procurement_method_details description, COALESCE(SUM(t.completed_lot_value), 0) result\n" +
            "FROM tender_prioritization t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY t.procurement_method_details\n" +
            "ORDER BY result DESC\n" +
            "LIMIT 5";

    private final String TOP_OKGZ_BY_AMOUNT_QUERY = "WITH tender_cpv AS (SELECT t.completed_lot_value,\n" +
            "                           unnest(t.cpv_list) cpv,\n" +
            "                           region,\n" +
            "                           passed_indicator_list,\n" +
            "                           contract_date,\n" +
            "                           procurement_method_details\n" +
            "                    FROM tender_prioritization t)\n" +
            "SELECT t.cpv description, COALESCE(SUM(t.completed_lot_value), 0) result\n" +
            "FROM tender_cpv t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY description\n" +
            "ORDER BY result DESC\n" +
            "LIMIT 5;";

    private final String TENDER_COUNT_BY_REGION_AND_INDICATOR_QUERY = "WITH tender_region_indicator AS (SELECT region,\n" +
            "                                        unnest(passed_indicator_list) indicator_id,\n" +
            "                                        procurement_method_details,\n" +
            "                                        cpv_list,\n" +
            "                                        contract_date\n" +
            "                                 FROM tender_prioritization)\n" +
            "SELECT t.region description, t.indicator_id, COUNT(t.indicator_id) AS value\n" +
            "FROM tender_region_indicator t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY description, t.indicator_id";

    private final String TENDER_AMOUNT_BY_REGION_AND_INDICATOR_QUERY = "WITH tender_region_indicator AS (SELECT region,\n" +
            "                                        unnest(passed_indicator_list) indicator_id,\n" +
            "                                        completed_lot_value,\n" +
            "                                        procurement_method_details,\n" +
            "                                        cpv_list,\n" +
            "                                        contract_date\n" +
            "                                 FROM tender_prioritization)\n" +
            "SELECT t.region description, t.indicator_id, COALESCE(SUM(t.completed_lot_value), 0) AS value\n" +
            "FROM tender_region_indicator t\n" +
            "WHERE TRUE %s\n" +
            "GROUP BY description, t.indicator_id;";

    private final String TENDER_COUNT_BY_MONTH_QUERY = "SELECT CONCAT(EXTRACT(YEAR FROM t.contract_date), '-', EXTRACT(MONTH FROM t.contract_date)) AS date,\n" +
            "       EXTRACT(YEAR FROM t.contract_date) as year,\n" +
            "       EXTRACT(MONTH FROM t.contract_date) as month,\n" +
            "       COUNT(t.id) AS value,\n" +
            "       COUNT(t.id) FILTER (WHERE t.passed_indicators_count > 0) AS value_with_risk\n" +
            "FROM tender_prioritization t\n" +
            "WHERE t.contract_date < date_trunc('month', CURRENT_DATE)\n" +
            "%s\n" +
            "GROUP BY year, month\n" +
            "order by year, month";

    private final String TENDER_AMOUNT_BY_MONTH_QUERY = "SELECT CONCAT(EXTRACT(YEAR FROM t.contract_date), '-', EXTRACT(MONTH FROM t.contract_date)) AS date,\n" +
            "       EXTRACT(YEAR FROM t.contract_date) as year,\n" +
            "       EXTRACT(MONTH FROM t.contract_date) as month,\n" +
            "       COALESCE(SUM(t.completed_lot_value), 0) AS value,\n" +
            "       COALESCE(SUM(t.completed_lot_value) FILTER (WHERE t.passed_indicators_count > 0), 0) AS value_with_risk\n" +
            "FROM tender_prioritization t\n" +
            "WHERE t.contract_date < date_trunc('month', CURRENT_DATE)\n" +
            "%s\n" +
            "GROUP BY year, month\n" +
            "order by year, month";

    private final String TOP_TENDER_QUERY = "select t.id\n" +
            "from tender_prioritization t\n" +
            "where true %s\n" +
            "order by t.passed_indicators_count desc, t.contract_date desc\n" +
            "limit 1";

    private final String TOP_BUYER_QUERY = "with buyer_inidicator as (select unnest(t.passed_indicator_list) as indicator_id, t.identifier_legal_name_ru\n" +
            "                          from tender_prioritization t\n" +
            "                          where t.passed_indicators_count > 0 %s),\n" +
            "     buyer_indicator_count as (select identifier_legal_name_ru, count(distinct indicator_id) as count\n" +
            "                               from buyer_inidicator\n" +
            "                               group by identifier_legal_name_ru)\n" +
            "select identifier_legal_name_ru from buyer_indicator_count\n" +
            "order by count desc, identifier_legal_name_ru\n" +
            "limit 1";

    private final String TOP_INDICATOR_QUERY = "with indicator_data as (select unnest(t.passed_indicator_list) as indicator, t.completed_lot_value\n" +
            "                        from tender_prioritization t\n" +
            "                        where true %s)\n" +
            "select indicator as result\n" +
            "from indicator_data\n" +
            "group by result\n" +
            "order by sum(completed_lot_value) desc\n" +
            "limit 1";

    @Override
    public PrioritizationInfoModel getPrioritizationInfo(DashboardBaseRequest request) {
        return template.queryForObject(String.format(
                PRIORITIZATION_INFO_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(PrioritizationInfoModel.class));
    }

    @Override
    public List<BaseTopModel> getTopRegionsByRiskTendersCount(DashboardBaseRequest request) {
        return template.query(String.format(
                TOP_REGIONS_BY_RISK_TENDERS_COUNT_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(BaseTopModel.class));
    }

    @Override
    public List<BaseTopModel> getTopMethodsByRiskTendersCount(DashboardBaseRequest request) {
        return template.query(String.format(
                TOP_METHODS_BY_RISK_TENDERS_COUNT_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(BaseTopModel.class));
    }

    @Override
    public List<BaseTopModel> getTopOkgzByRiskTendersCount(DashboardBaseRequest request) {
        return template.query(String.format(
                TOP_OKGZ_BY_RISK_TENDERS_COUNT_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(BaseTopModel.class));
    }

    @Override
    public List<BaseTopModel> getTopRegionsByAmount(DashboardBaseRequest request) {
        return template.query(String.format(
                TOP_REGIONS_BY_AMOUNT_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(BaseTopModel.class));
    }

    @Override
    public List<BaseTopModel> getTopMethodsByAmount(DashboardBaseRequest request) {
        return template.query(String.format(
                TOP_METHODS_BY_AMOUNT_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(BaseTopModel.class));
    }

    @Override
    public List<BaseTopModel> getTopOkgzByAmount(DashboardBaseRequest request) {
        return template.query(String.format(
                TOP_OKGZ_BY_AMOUNT_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(BaseTopModel.class));
    }

    @Override
    public List<ValueByFieldModel> getTendersCountByRegionAndIndicator(DashboardBaseRequest request) {
        return template.query(String.format(
                TENDER_COUNT_BY_REGION_AND_INDICATOR_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(ValueByFieldModel.class));
    }

    @Override
    public List<ValueByFieldModel> getTendersAmountByRegionAndIndicator(DashboardBaseRequest request) {
        return template.query(String.format(
                TENDER_AMOUNT_BY_REGION_AND_INDICATOR_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(ValueByFieldModel.class));
    }

    @Override
    public List<ValueByMonthModel> getTendersCountByMonth(DashboardBaseRequest request) {
        return template.query(String.format(
                TENDER_COUNT_BY_MONTH_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(ValueByMonthModel.class));
    }

    @Override
    public List<ValueByMonthModel> getTendersAmountByMonth(DashboardBaseRequest request) {
        return template.query(String.format(
                TENDER_AMOUNT_BY_MONTH_QUERY,
                getPeriodFilter(request)), new BeanPropertyRowMapper<>(ValueByMonthModel.class));
    }

    @Override
    public Long getTopTender(DashboardBaseRequest request) {
        return template.queryForObject(String.format(
                TOP_TENDER_QUERY,
                getPeriodFilter(request)), Long.class);
    }

    @Override
    public String getTopBuyer(DashboardBaseRequest request) {
        return template.queryForObject(String.format(
                TOP_BUYER_QUERY,
                getPeriodFilter(request)), String.class);
    }

    @Override
    public Integer getTopIndicator(DashboardBaseRequest request) {
        return template.queryForObject(String.format(
                TOP_INDICATOR_QUERY,
                getPeriodFilter(request)), Integer.class);
    }

    private String getPeriodFilter(DashboardBaseRequest request) {
        StringBuilder period = new StringBuilder();
        if (nonNull(request.getContractStartDate()) || nonNull(request.getContractEndDate())) {
            if (nonNull(request.getContractStartDate())) {
                period
                        .append(" AND t.contract_date >= '")
                        .append(request.getContractStartDate())
                        .append("'");
            }
            if (nonNull(request.getContractEndDate())) {
                period
                        .append(" AND t.contract_date <= '")
                        .append(request.getContractEndDate())
                        .append("'");
            }
        } else {
            period.append(" AND EXTRACT(YEARS FROM t.contract_date) = EXTRACT(YEARS FROM now())");
        }
        return period.toString();
    }
}
