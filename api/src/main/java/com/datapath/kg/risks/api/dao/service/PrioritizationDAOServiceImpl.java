package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.Utils;
import com.datapath.kg.risks.api.dao.PrioritizationExportHandler;
import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.BuyerPrioritizationEntity;
import com.datapath.kg.risks.api.dao.entity.TenderPrioritizationEntity;
import com.datapath.kg.risks.api.dto.SortingOptionDTO;
import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;
import org.apache.poi.ss.usermodel.Sheet;
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
public class PrioritizationDAOServiceImpl implements PrioritizationDAOService {

    private static final String TENDER_PRIORITIZATION = "with tender_prioritization_checklist as (\n" +
            "  select tp.*, case when exists(select * from checklist c where c.auditor_id = %s and c.tender_id = tp.id)\n" +
            "                         then (select c.id from checklist c where c.auditor_id = %s and c.tender_id = tp.id order by status_id, modified_date desc limit 1)\n" +
            "      else (select c.id from checklist c where c.tender_id = tp.id order by status_id desc, modified_date desc limit 1) end checklist_id\n" +
            "  from tender_prioritization tp\n" +
            ") SELECT tp.*,\n" +
            "         (select c.id from checklist c where c.id = tp.checklist_id) as checklist_id,\n" +
            "         (select c.auditor_id from checklist c where c.id = tp.checklist_id) as auditor_id,\n" +
            "         (select c.name from checklist c where c.id = tp.checklist_id) as checklist_name,\n" +
            "         (select c.status_id from checklist c where c.id = tp.checklist_id) as checklist_status,\n" +
            "         (select a.name from checklist c join auditor a on c.auditor_id = a.id where c.id = tp.checklist_id) as checklist_auditor_name," +
            "         (ROW_NUMBER() over (ORDER BY completed_lot_value)  * %s +\n" +
            "         ROW_NUMBER() over (ORDER BY risk_level, completed_lot_value) * %s) :: numeric(20, 2) AS weighted_rank\n" +
            "    FROM tender_prioritization_checklist tp\n" +
            "%s\n" +
            "%s\n" +
            "%s";

    private static final String BUYERS_PRIORITIZATION = "WITH buyers AS (SELECT b.id,\n" +
            "                       b.identifier_id,\n" +
            "                       b.identifier_legal_name_ru,\n" +
            "                       b.region,\n" +
            "                       (SELECT level\n" +
            "                        FROM buyer_risk_level_range\n" +
            "                        WHERE (CASE\n" +
            "                                 WHEN SUM(t.completed_lot_value) = 0 THEN 0\n" +
            "                                 ELSE SUM(t.completed_lot_value :: DOUBLE PRECISION * t.risk_level) /\n" +
            "                                      SUM(t.completed_lot_value) END) > left_bound\n" +
            "                          AND\n" +
            "\n" +
            "                            (CASE\n" +
            "                               WHEN SUM(t.completed_lot_value) = 0 THEN 0\n" +
            "                               ELSE SUM(t.completed_lot_value :: DOUBLE PRECISION * t.risk_level) /\n" +
            "                                    SUM(t.completed_lot_value) END) <= right_bound\n" +
            "                       ) risk_level,\n" +
            "                       COUNT(t.id)                                                                        procedure_number,\n" +
            "                       COALESCE(SUM(t.procedure_expected_value), 0)                                       procedures_expected_value,\n" +
            "                       COALESCE(SUM(t.completed_lot_value), 0)                                            contracts_amount,\n" +
            "                       COUNT(*) FILTER (WHERE t.passed_indicators_count >\n" +
            "                                              0)                                                          risked_procedures,\n" +
            "                       COUNT(*) FILTER (WHERE t.passed_indicators_count > 0 AND\n" +
            "                                              t.current_stage =\n" +
            "                                              'contractSigned')                                           risked_procedures_with_contract,\n" +
            "                       COALESCE(SUM(t.completed_lot_value) FILTER (WHERE t.passed_indicators_count >\n" +
            "                                                                     0), 0)                               risked_procedures_expected_value,\n" +
            "                       COALESCE(SUM(t.completed_lot_value) FILTER (WHERE t.passed_indicators_count > 0 AND current_stage =\n" +
            "                                                                                   'contractSigned'), 0)  risked_procedures_expected_value_with_contract,\n" +
            "                       COALESCE(SUM(t.completed_lots_count), 0)                                           completed_lots_count,\n" +
            "                       COALESCE(SUM(t.contracts_count), 0)                                                contracts_count,\n" +
            "                       COALESCE(SUM(t.winner_bids_value), 0)                                              winner_bids_value,\n" +
            "                       (SELECT count(buyer_indicators.indicator)\n" +
            "                       FROM (SELECT DISTINCT unnest(passed_indicator_list) indicator\n" +
            "                              FROM tender_prioritization t\n" +
            "                              WHERE t.buyer_id = b.id %s) AS buyer_indicators)                                 passed_indicators_count,\n" +
            "                       (SELECT array_to_string(array_agg((SELECT name FROM indicator WHERE id = il.i)), ', ')\n" +
            "                              FROM (SELECT DISTINCT unnest(passed_indicator_list) i\n" +
            "                                    FROM tender_prioritization t\n" +
            "                                    WHERE t.buyer_id = b.id %s) AS il)                                         passed_indicators,\n" +
            "                       b.procedures_with_bad_data\n" +
            "                FROM buyer_prioritization b\n" +
            "                       JOIN tender_prioritization t ON b.id = t.buyer_id\n" +
            "                %s\n" +
            "                GROUP BY b.id)\n" +
            "SELECT buyers.*, \n" +
            "(SELECT description FROM buyer_risk_level_range WHERE level = buyers.risk_level) range_desc, \n" +
            "(ROW_NUMBER() over (ORDER BY winner_bids_value) * %f +\n" +
            "          ROW_NUMBER() over (ORDER BY risk_level, winner_bids_value) * %f) :: numeric(20, 2) AS weighted_rank\n" +
            "FROM buyers\n" +
            "%s\n" +
            "%s\n" +
            "%s";

    private static final String TENDER_PRIORITIZATION_BY_ID_AND_CONTRACT_DATE = "select * from tender_prioritization where id = %s " +
            "and contract_date between '%s' and '%s'";

    private static final String TENDER_PRIORITIZATION_BY_ID = "SELECT * FROM tender_prioritization WHERE id = %s";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public TenderPrioritizationEntity getTenderPrioritizationById(Integer id) {
        List<TenderPrioritizationEntity> tenders = jdbcTemplate.query(String.format(TENDER_PRIORITIZATION_BY_ID, id),
                new BeanPropertyRowMapper<>(TenderPrioritizationEntity.class));
        return isEmpty(tenders) ? null : tenders.get(0);
    }

    @Override
    public TenderPrioritizationEntity getByIdAndContractDate(Integer id, String startDate, String endDate) {
        List<TenderPrioritizationEntity> tenders = jdbcTemplate.query(String.format(TENDER_PRIORITIZATION_BY_ID_AND_CONTRACT_DATE, id, startDate, endDate),
                new BeanPropertyRowMapper<>(TenderPrioritizationEntity.class));
        return isEmpty(tenders) ? null : tenders.get(0);
    }

    @Override
    public List<TenderPrioritizationEntity> getTenders(TenderPrioritizationRequest request, AuditorEntity auditor) {
        return jdbcTemplate.query(String.format(TENDER_PRIORITIZATION,
                auditor.getId(),
                auditor.getId(),
                auditor.getTenderValueRank().toString(),
                auditor.getTenderRiskLevelRank().toString(),
                getTenderFilters(request),
                getSortingOptions(request.getSortingOptions()),
                getLimit(1000)),
                new BeanPropertyRowMapper<>(TenderPrioritizationEntity.class));
    }

    @Override
    public List<BuyerPrioritizationEntity> getBuyers(BuyerPrioritizationRequest request, AuditorEntity auditor) {
        return jdbcTemplate.query(String.format(BUYERS_PRIORITIZATION,
                getBuyerTenderFilter(request),
                getBuyerTenderFilter(request),
                getBuyerFilters(request),
                auditor.getBuyerValueRank(),
                auditor.getBuyerRiskLevelRank(),
                getBuyerRiskLevelFilter(request.getRiskLevel()),
                getSortingOptions(request.getSortingOptions()),
                getLimit(1000)),
                new BeanPropertyRowMapper<>(BuyerPrioritizationEntity.class));
    }

    @Override
    public void exportBuyers(BuyerPrioritizationRequest request, AuditorEntity auditor, Sheet sheet) {
        jdbcTemplate.query(String.format(BUYERS_PRIORITIZATION,
                getBuyerTenderFilter(request),
                getBuyerTenderFilter(request),
                getBuyerFilters(request),
                auditor.getBuyerValueRank(),
                auditor.getBuyerRiskLevelRank(),
                getBuyerRiskLevelFilter(request.getRiskLevel()),
                getSortingOptions(request.getSortingOptions()),
                getLimit(1000)),
                new PrioritizationExportHandler(request.getColumns(), request.getSelectedExportIds(), sheet));
    }

    @Override
    public void exportTenders(TenderPrioritizationRequest request, AuditorEntity auditor, Sheet sheet) {
        jdbcTemplate.query(String.format(TENDER_PRIORITIZATION,
                auditor.getId(),
                auditor.getId(),
                auditor.getTenderValueRank().toString(),
                auditor.getTenderRiskLevelRank().toString(),
                getTenderFilters(request),
                getSortingOptions(request.getSortingOptions()),
                getLimit(1000)),
                new PrioritizationExportHandler(request.getColumns(), request.getSelectedExportIds(), sheet));
    }

    private String getLimit(int count) {
        return count == 0 ? "" : "LIMIT " + count;
    }

    private String getTenderFilters(TenderPrioritizationRequest request) {
        StringBuilder filters = new StringBuilder("WHERE TRUE");

        if (!isEmpty(request.getBuyerIds())) {
            String buyerIds = request.getBuyerIds()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND tp.buyer_id IN (")
                    .append(buyerIds)
                    .append(")");
        }

        if (!isEmpty(request.getProcurementMethodDetails())) {
            String procurementMethodDetails = request.getProcurementMethodDetails()
                    .stream()
                    .map(method -> "'" + method + "'")
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND tp.procurement_method_details IN (")
                    .append(procurementMethodDetails)
                    .append(")");
        }

        if (!isEmpty(request.getCpv())) {
            String cpv = request.getCpv()
                    .stream()
                    .map(c -> "\"" + c + "\"")
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND tp.cpv_list && '{")
                    .append(cpv)
                    .append("}'");
        }

        if (isNull(request.getIndicatorStatus())) {
            if (!isEmpty(request.getIndicators())) {
                String indicators = request.getIndicators()
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(", "));

                filters
                        .append(" AND tp.passed_indicator_list && '{")
                        .append(indicators)
                        .append("}'");
            }
        } else {
            if (request.getIndicatorStatus().equals(1)) {
                filters.append(" AND tp.passed_indicators_count > 0");
            } else {
                filters.append(" AND tp.passed_indicators_count = 0");
            }
        }

        if (nonNull(request.getCompletedLotValueMin())) filters
                .append(" AND tp.completed_lot_value >= ")
                .append(request.getCompletedLotValueMin());

        if (nonNull(request.getCompletedLotValueMax())) filters
                .append(" AND tp.completed_lot_value <= ")
                .append(request.getCompletedLotValueMax());

        if (!isEmpty(request.getRiskLevel())) {
            String riskLevels = request.getRiskLevel()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND tp.risk_level IN (")
                    .append(riskLevels)
                    .append(")");
        }

        if (nonNull(request.getContractStartDate())) filters
                .append(" AND tp.contract_date >= '")
                .append(request.getContractStartDate())
                .append("'");

        if (nonNull(request.getContractEndDate())) filters
                .append(" AND tp.contract_date <= '")
                .append(request.getContractEndDate())
                .append("'");

//        if (!isEmpty(request.getSelectedExportIds())) {
//            String tenders = request.getSelectedExportIds()
//                    .stream()
//                    .map(String::valueOf)
//                    .collect(Collectors.joining(", "));
//            filters
//                    .append(" AND tp.id IN (")
//                    .append(tenders)
//                    .append(")");
//        }

        if (nonNull(request.getTenderId())) filters
                .append(" AND tp.id = ")
                .append(request.getTenderId());

        if (WITHOUT_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE.equals(request.getChecklistType())) {
            filters.append(" AND tp.id NOT IN (SELECT c.tender_id FROM checklist c WHERE c.tender_id IS NOT NULL)");
        } else if (WITH_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE.equals(request.getChecklistType()) ||
                !isEmpty(request.getAuditorIds()) ||
                nonNull(request.getChecklistStatus())) {

            filters.append(" AND tp.id IN (SELECT c.tender_id FROM checklist c WHERE c.tender_id IS NOT NULL");

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

    private String getBuyerFilters(BuyerPrioritizationRequest request) {
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


//        if (!isEmpty(request.getSelectedExportIds())) {
//            String buyers = request.getSelectedExportIds()
//                    .stream()
//                    .map(String::valueOf)
//                    .collect(Collectors.joining(", "));
//            filters
//                    .append(" AND b.id IN (")
//                    .append(buyers)
//                    .append(")");
//        } else {
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

        filters.append(getBuyerTenderFilter(request));

        return filters.toString();
    }

    private String getBuyerTenderFilter(BuyerPrioritizationRequest request) {
        StringBuilder filters = new StringBuilder();
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

        if (!isEmpty(request.getIndicators())) {
            String indicators = request.getIndicators()
                    .stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND t.passed_indicator_list && '{")
                    .append(indicators)
                    .append("}'");
        }

        return filters.toString();
    }

    private String getBuyerRiskLevelFilter(List<Integer> riskLevel) {
        StringBuilder filters = new StringBuilder("WHERE TRUE");

        if (!isEmpty(riskLevel)) {
            String riskLevels = riskLevel.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            filters
                    .append(" AND risk_level IN (")
                    .append(riskLevels)
                    .append(")");
        }
        return filters.toString();
    }

    private String getSortingOptions(List<SortingOptionDTO> sortingOptions) {
        StringBuilder options = new StringBuilder("ORDER BY ");

        if (!isEmpty(sortingOptions)) {
            options.append(
                    sortingOptions
                            .stream()
                            .map(option -> Utils.convertCase(option.getField()) + " " + option.getType())
                            .collect(Collectors.joining(", "))
            );
            options.append(", ");
        }

        options.append("weighted_rank DESC");

        return options.toString();
    }
}
