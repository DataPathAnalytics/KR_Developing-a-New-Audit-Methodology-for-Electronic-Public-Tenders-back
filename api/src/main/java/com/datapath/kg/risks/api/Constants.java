package com.datapath.kg.risks.api;

import com.datapath.kg.risks.api.dto.DashboardTenderChecklistFilterDTO;
import com.datapath.kg.risks.api.dto.IndicatorStatusDTO;
import com.datapath.kg.risks.api.dto.ProcurementMethodDetailsDTO;
import com.datapath.kg.risks.api.dto.RegionDTO;
import lombok.Data;

import java.util.*;

@Data
public class Constants {

    public static final String ID = "id";
    public static final String PROCUREMENT_METHOD_DETAILS_TITLE = "procurementMethodDetails";
    public static final String CURRENT_STAGE_TITLE = "currentStage";
    public static final String RISK_LEVEL_TITLE = "riskLevel";
    public static final String PRIORITIZATION_PARAMETER_TITLE = "weightedRank";
    public static final String CHECKLIST_STATUS_TITLE = "checklistStatus";

    public static final Integer WITH_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE = 1;
    public static final Integer WITHOUT_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE = 2;

    public static final String RISK_LEVEL_DESCRIPTION_COLUMN_NAME = "range_desc";

    public static final Integer COMPLETED_CHECKLIST_STATUS_ID = 2;

    public static final String USER_IS_DISABLED_ERROR_JSON_MESSAGE = "{\"errorMessageCode\": 2}";
    public static final String BAD_CREDENTIALS_ERROR_JSON_MESSAGE = "{\"errorMessageCode\": 1}";

    public static final List<ProcurementMethodDetailsDTO> PROCUREMENT_METHOD_DETAILS_MAPPING = Collections.unmodifiableList(
            Arrays.asList(
                    new ProcurementMethodDetailsDTO("simplicated", "Simplified Acquisition", "Упрощенный"),
                    new ProcurementMethodDetailsDTO("oneStage", "Single-Stage Bidding", "Одноэтапный"),
                    new ProcurementMethodDetailsDTO("downgrade", "Reverse Auction", "На понижение цены"),
                    new ProcurementMethodDetailsDTO("singleSource", "Direct Contracting", "Прямого заключения договора")
            )
    );

    public static final Map<String, String> PROCUREMENT_METHOD_DETAILS_MAP = new HashMap<String, String>() {{
        put("simplicated", "Упрощенный");
        put("oneStage", "Одноэтапный");
        put("downgrade", "На понижение цены");
        put("singleSource", "Прямого заключения договора");
    }};

    public static final Map<String, String> CURRENT_STAGE_MAP = new HashMap<String, String>() {{
        put("published", "Опубликован");
        put("changed", "Изменен");
        put("cancelled", "Отменен");
        put("evaluationComplete", "Оценка завершена");
        put("contractSigned", "Договоры подписаны");
        put("bidsOpened", "Опубликован протокол вскрытия");
        put("autoProlonged", "Автоматически продлен");
        put("evaluationResultsPending", "Ожидание итогов оценки");
        put("contractSignPending", "Ожидание подписания договоров");
    }};

    public static final List<String> NUMBER_TYPE_FIELD_LIST = Collections.unmodifiableList(
            Arrays.asList(
                    "guaranteeAmount",
                    "tenderersCount",
                    "disqualifiedsCount",
                    "suppliersCount",
                    "completedLotsCount",
                    "procedureExpectedValue",
                    "completedLotValue",
                    "complaintsCount",
                    "passedIndicatorsCount",
                    "procedureNumber",
                    "contractsCount",
                    "proceduresExpectedValue",
                    "winnerBidsValue",
                    "contractsAmount",
                    "riskedProcedures",
                    "riskedProceduresWithContract",
                    "riskedProceduresExpectedValue",
                    "riskedProceduresExpectedValueWithContract",
                    "proceduresWithBadData"
            )
    );

    public static final List<DashboardTenderChecklistFilterDTO> TENDER_DASHBOARD_CHECKLIST_FILTER = Collections.unmodifiableList(
            Arrays.asList(
                    new DashboardTenderChecklistFilterDTO(
                            WITH_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE,
                            "Только с контрольными листами",
                            "Only with checklists"),
                    new DashboardTenderChecklistFilterDTO(
                            WITHOUT_CHECKLIST_TENDER_DASHBOARD_FILTER_TYPE,
                            "Только без контрольных листов",
                            "Only w/o checklists")
            )
    );

    public static final Map<Integer, String> CHECKLIST_STATUS = new HashMap<Integer, String>(){{
        put(1, "активный");
        put(2, "завершенный");
    }};

    public static final List<RegionDTO> REGIONS = Collections.unmodifiableList(
            Arrays.asList(
                    new RegionDTO("Джалал-Абадская Область", "Джалал-Абадская Область", "Dzhalal-Abadskaya region"),
                    new RegionDTO("Нарынская Область", "Нарынская Область", "Narynskaya region"),
                    new RegionDTO("Бишкек", "Бишкек", "Bishkek"),
                    new RegionDTO("Чуйская Область", "Чуйская Область", "Chuyskaya region"),
                    new RegionDTO("Баткенская Область", "Баткенская Область", "Batkenskaya region"),
                    new RegionDTO("Ош", "Ош", "Osh"),
                    new RegionDTO("Таласская Область", "Таласская Область", "Talasskaya region"),
                    new RegionDTO("Ошская Область", "Ошская Область", "Oshskaya region"),
                    new RegionDTO("Иссык-Кульская Область", "Иссык-Кульская Область", "Issyk-Kulskaya region")
            )
    );

    public static final List<IndicatorStatusDTO> INDICATOR_STATUS = Collections.unmodifiableList(
            Arrays.asList(
                    new IndicatorStatusDTO(1, "Только конкурсы с риском", "Only Tenders with Risk"),
                    new IndicatorStatusDTO(2, "Только конкурсы без риска", "Only Tenders w/o Risk")
            )
    );
}
