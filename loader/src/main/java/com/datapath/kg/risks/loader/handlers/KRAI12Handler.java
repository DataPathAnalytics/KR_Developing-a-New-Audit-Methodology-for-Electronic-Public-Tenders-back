package com.datapath.kg.risks.loader.handlers;

import com.datapath.kg.risks.loader.dao.entity.TenderEntity;
import com.datapath.kg.risks.loader.dao.entity.indicators.TenderIndicatorEntity;
import com.datapath.kg.risks.loader.dao.service.TenderDAOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.datapath.kg.risks.loader.utils.Constants.*;
import static com.datapath.kg.risks.loader.utils.Indicator.KRAI12;

@Component
@Slf4j
public class KRAI12Handler extends KRAIBaseHandler {

    @Autowired
    private TenderDAOService dao;

    @Override
    @Transactional
    public void handle() {
        log.info("Start handle KRAI 12 indicator");

        List<Integer> tenderIds = dao.getKRAI12AcceptableTenders();
        for (Integer tenderId : tenderIds) {
            log.info("Process {} of {} tenders", tenderIds.indexOf(tenderId), tenderIds.size());
            TenderEntity tender = dao.getTender(tenderId);
            if (isAcceptable(tender)) {
                TenderIndicatorEntity indicator = buildTenderIndicator(tender, KRAI12);

                if (!tender.isBadQuality() && !tender.isHasDocuments()) {
                    indicator.setIndicatorValue(PASSED);
                }
                indicatorDAOService.save(indicator);
            }
        }
        log.info("Finish handle KRAI 12 indicator");
    }

    private boolean isAcceptable(TenderEntity tender) {
        String procurementMethodDetails = tender.getProcurementMethodDetails();
        String status = tender.getStatus();
        String currentStage = tender.getCurrentStage();

        return ONE_STAGE.equalsIgnoreCase(procurementMethodDetails) &&
                (COMPLETE.equalsIgnoreCase(status) || (ACTIVE.equalsIgnoreCase(status) &&
                        EVALUATION_COMPLETE.equalsIgnoreCase(currentStage) &&
                        hasAwardOldestThan30Days(tender)));
    }
}
