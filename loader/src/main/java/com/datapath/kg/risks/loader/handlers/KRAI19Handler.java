package com.datapath.kg.risks.loader.handlers;

import com.datapath.kg.risks.loader.dao.entity.TenderEntity;
import com.datapath.kg.risks.loader.dao.entity.indicators.TenderIndicatorEntity;
import com.datapath.kg.risks.loader.dao.entity.tv.ActiveCPV;
import com.datapath.kg.risks.loader.dao.entity.tv.TenderCpvList;
import com.datapath.kg.risks.loader.dao.service.TenderDAOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.datapath.kg.risks.loader.utils.Constants.*;
import static com.datapath.kg.risks.loader.utils.Indicator.KRAI19;

@Component
@Slf4j
public class KRAI19Handler extends KRAIBaseHandler {

    @Autowired
    private TenderDAOService dao;

    @Override
    @Transactional
    public void handle() {
        log.info("Start handle KRAI 19 indicator");

        List<Integer> tenderIds = dao.getKRAI19AcceptableTenders();

        for (Integer tenderId : tenderIds) {
            log.info("Process {} of {} tenders (tenderId = {})", tenderIds.indexOf(tenderId), tenderIds.size(), tenderId);
            TenderEntity tender = dao.getTender(tenderId);
            if (isAcceptable(tender)) {
                TenderIndicatorEntity indicatorEntity = buildTenderIndicator(tender, KRAI19);

                if (!tender.isBadQuality()) {
                    List<TenderCpvList> tenderCpv = variablesDAOService.getTenderCpvListByTenderId(tender.getId());
                    List<ActiveCPV> activeCpvList = variablesDAOService.getActiveCpvByBuyerAndYearAndDate(
                            tender.getBuyerId(),
                            tender.getDatePublished().getYear(),
                            tender.getDate()
                    );

                    for (ActiveCPV active : activeCpvList) {
                        boolean exist = tenderCpv
                                .stream()
                                .anyMatch(cpv ->
                                        active.getCpv().equalsIgnoreCase(cpv.getCpv()) &&
                                                tender.getDatePublished().isAfter(active.getDate()));

                        if (exist) {
                            indicatorEntity.setIndicatorValue(PASSED);
                            break;
                        }
                    }
                }

                indicatorDAOService.save(indicatorEntity);
            }
        }
        log.info("Finish handle KRAI 19 indicator");
    }

    private boolean isAcceptable(TenderEntity tender) {
        String procurementMethodDetails = tender.getProcurementMethodDetails();
        String status = tender.getStatus();
        String currentStage = tender.getCurrentStage();

        return (ONE_STAGE.equalsIgnoreCase(procurementMethodDetails) ||
                SIMPLICATED.equalsIgnoreCase(procurementMethodDetails) ||
                DOWNGRADE.equalsIgnoreCase(procurementMethodDetails))
                &&
                (COMPLETE.equalsIgnoreCase(status) || (ACTIVE.equalsIgnoreCase(status) &&
                        EVALUATION_COMPLETE.equalsIgnoreCase(currentStage) &&
                        hasAwardOldestThan30Days(tender)));
    }
}
