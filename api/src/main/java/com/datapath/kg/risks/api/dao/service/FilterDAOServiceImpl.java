package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.datapath.kg.risks.api.Constants.WITHOUT_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE;
import static com.datapath.kg.risks.api.Constants.WITH_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class FilterDAOServiceImpl implements FilterDAOService {

    private static final String TENDER_PRIORITIZATION_INDICATOR_FILTER =
            "select distinct unnest(passed_indicator_list) as i\n" +
            "from tender_prioritization t\n" +
            "left join checklist c on c.tender_id = t.id\n" +
            "%s\n" +
            "order by i";

    private static final String BUYER_PRIORITIZATION_INDICATOR_FILTER =
            "select distinct unnest(passed_indicator_list) as i\n" +
            "from tender_prioritization t\n" +
            "join buyer_prioritization b on t.buyer_id = b.id\n" +
            "%s\n" +
            "order by i";

    private JdbcTemplate jdbcTemplate;

    public FilterDAOServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Integer> getAvailableIndicatorsForTenderPrioritization(TenderPrioritizationRequest request) {
        return jdbcTemplate.queryForList(
                String.format(TENDER_PRIORITIZATION_INDICATOR_FILTER, generateTenderFilters(request)),
                Integer.class);
    }

    @Override
    public List<Integer> getAvailableIndicatorsForBuyerPrioritization(BuyerPrioritizationRequest request) {
        return jdbcTemplate.queryForList(
                String.format(BUYER_PRIORITIZATION_INDICATOR_FILTER, generateBuyerFilters(request)),
                Integer.class);
    }

    private String generateBuyerFilters(BuyerPrioritizationRequest request) {
        StringBuilder filters = new StringBuilder("WHERE TRUE");

        if (!isEmpty(request.getRegions())) {
            String regions = request.getRegions()
                    .stream()
                    .map(region -> "'" + region + "'")
                    .collect(Collectors.joining(", "));

            filters.append(" AND b.region IN (")
                    .append(regions)
                    .append(")");
        }

        if (!isEmpty(request.getBuyerIds())) {
            String buyers = request.getBuyerIds()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND b.id IN (")
                    .append(buyers)
                    .append(")");
        }

        if (nonNull(request.getContractStartDate())) filters
                .append(" AND t.contract_date >= '")
                .append(request.getContractStartDate())
                .append("'");

        if (nonNull(request.getContractEndDate())) filters
                .append(" AND t.contract_date <= '")
                .append(request.getContractEndDate())
                .append("'");

        if (!isEmpty(request.getProcurementMethodDetails())) {
            String procurementMethodDetails = request.getProcurementMethodDetails()
                    .stream()
                    .map(method -> "'" + method + "'")
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND t.procurement_method_details IN (")
                    .append(procurementMethodDetails)
                    .append(")");
        }

        if (!isEmpty(request.getCpv())) {
            String cpv = request.getCpv()
                    .stream()
                    .map(c -> "\"" + c + "\"")
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND t.cpv_list && '{")
                    .append(cpv)
                    .append("}'");
        }

        return filters.toString();
    }

    private String generateTenderFilters(TenderPrioritizationRequest request) {
        StringBuilder filters = new StringBuilder("WHERE TRUE");
        if (!isEmpty(request.getBuyerIds())) {
            String buyerIds = request.getBuyerIds()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND t.buyer_id IN (")
                    .append(buyerIds)
                    .append(")");
        }

        if (!isEmpty(request.getProcurementMethodDetails())) {
            String procurementMethodDetails = request.getProcurementMethodDetails()
                    .stream()
                    .map(method -> "'" + method + "'")
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND t.procurement_method_details IN (")
                    .append(procurementMethodDetails)
                    .append(")");
        }

        if (!isEmpty(request.getCpv())) {
            String cpv = request.getCpv()
                    .stream()
                    .map(c -> "\"" + c + "\"")
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND t.cpv_list && '{")
                    .append(cpv)
                    .append("}'");
        }

        if (nonNull(request.getCompletedLotValueMin())) filters
                .append(" AND t.completed_lot_value >= ")
                .append(request.getCompletedLotValueMin());

        if (nonNull(request.getCompletedLotValueMax())) filters
                .append(" AND t.completed_lot_value <= ")
                .append(request.getCompletedLotValueMax());

        if (!isEmpty(request.getRiskLevel())) {
            String riskLevels = request.getRiskLevel()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND t.risk_level IN (")
                    .append(riskLevels)
                    .append(")");
        }

        if (nonNull(request.getContractStartDate())) filters
                .append(" AND t.contract_date >= '")
                .append(request.getContractStartDate())
                .append("'");

        if (nonNull(request.getContractEndDate())) filters
                .append(" AND t.contract_date <= '")
                .append(request.getContractEndDate())
                .append("'");

        if (nonNull(request.getTenderId())) filters
                .append(" AND t.id = ")
                .append(request.getTenderId());

        if (WITHOUT_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE.equals(request.getChecklistType())) {
            filters.append(" AND t.id NOT IN (SELECT c.tender_id FROM checklist c WHERE c.tender_id IS NOT NULL)");
        } else if (WITH_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE.equals(request.getChecklistType()) ||
                !isEmpty(request.getAuditorIds()) ||
                nonNull(request.getChecklistStatus())) {

            filters.append(" AND t.id IN (SELECT c.tender_id FROM checklist c WHERE c.tender_id IS NOT NULL");

            if (!isEmpty(request.getAuditorIds())) {
                String auditorIds = request.getAuditorIds()
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));

                filters
                        .append(" AND c.auditor_id IN (")
                        .append(auditorIds)
                        .append(")");
            }
            if (nonNull(request.getChecklistStatus())) {
                filters
                        .append(" AND c.status_id = ")
                        .append(request.getChecklistStatus());
            }
            filters.append(")");
        }

        return filters.toString();
    }
}
