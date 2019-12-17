package com.datapath.kg.risks.loader.handlers;

import com.datapath.kg.risks.loader.dao.entity.AwardEntity;
import com.datapath.kg.risks.loader.dao.entity.TenderEntity;
import com.datapath.kg.risks.loader.dao.entity.indicators.TenderIndicatorEntity;
import com.datapath.kg.risks.loader.dao.service.TenderDAOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.datapath.kg.risks.loader.utils.Constants.*;
import static com.datapath.kg.risks.loader.utils.Indicator.KRAI13;
import static java.util.Objects.nonNull;

@Component
@Slf4j
public class KRAI13Handler extends KRAIBaseHandler {

    private static final Double THRESHOLD_AMOUNT = 1000000d;
    @Autowired
    private TenderDAOService dao;

    @Override
    @Transactional
    public void handle() {
        log.info("Start handle KRAI 13 indicator");
        List<Integer> tenderIds = dao.getKRAI13AcceptableTenders();

        for (Integer tenderId : tenderIds) {
            log.info("Process {} of {} tenders", tenderIds.indexOf(tenderId), tenderIds.size());
            TenderEntity tender = dao.getTender(tenderId);
            if (isAcceptable(tender)) {
                TenderIndicatorEntity indicator = buildTenderIndicator(tender, KRAI13);

                if (!tender.isBadQuality()) {
                    Double awardAmountSum = tender.getAwards().stream()
                            .filter(award -> ACTIVE.equalsIgnoreCase(award.getStatus()) && nonNull(award.getValueAmount()))
                            .mapToDouble(AwardEntity::getValueAmount)
                            .sum();

                    if (awardAmountSum > THRESHOLD_AMOUNT) {
                        indicator.setIndicatorValue(PASSED);
                    }
                }

                indicatorDAOService.save(indicator);
            }
        }
        log.info("Start handle KRAI 13 indicator");
    }

    private boolean isAcceptable(TenderEntity tender) {
        String procurementMethodDetails = tender.getProcurementMethodDetails();
        String status = tender.getStatus();
        String currentStage = tender.getCurrentStage();

        return SIMPLICATED.equalsIgnoreCase(procurementMethodDetails)
                &&
                (COMPLETE.equalsIgnoreCase(status) || (ACTIVE.equalsIgnoreCase(status) &&
                        EVALUATION_COMPLETE.equalsIgnoreCase(currentStage) &&
                        hasAwardOldestThan30Days(tender)));
    }
}
