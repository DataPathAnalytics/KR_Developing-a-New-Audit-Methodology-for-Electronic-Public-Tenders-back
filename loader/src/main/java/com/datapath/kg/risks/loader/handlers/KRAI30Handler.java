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
import java.util.Objects;
import java.util.Set;

import static com.datapath.kg.risks.loader.utils.Constants.*;
import static com.datapath.kg.risks.loader.utils.Indicator.KRAI30;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class KRAI30Handler extends KRAIBaseHandler {

    @Autowired
    private TenderDAOService dao;

    @Override
    @Transactional
    public void handle() {
        log.info("Start handle KRAI 30 indicator");

        List<Integer> tenderIds = dao.getKRAI30AcceptableTenders();

        for (Integer tenderId : tenderIds) {
            log.info("Process {} of {} tenders", tenderIds.indexOf(tenderId), tenderIds.size());
            TenderEntity tender = dao.getTender(tenderId);

            if (isAcceptable(tender)) {
                TenderIndicatorEntity indicator = buildTenderIndicator(tender, KRAI30);
                if (!tender.isBadQuality()) {
                    Set<AwardEntity> awards = tender.getAwards();
                    if (!isEmpty(awards)) {

                        List<AwardEntity> awardWithStatusActiveOrPending = awards.stream()
                                .filter(award -> ACTIVE.equalsIgnoreCase(award.getStatus()) ||
                                        PENDING.equalsIgnoreCase(award.getStatus()))
                                .collect(toList());

                        if (awardsMoreThan4(awardWithStatusActiveOrPending)) {

                            long countOfUniqueRelatedBid = awardWithStatusActiveOrPending
                                    .stream()
                                    .map(AwardEntity::getBid)
                                    .filter(Objects::nonNull)
                                    .distinct()
                                    .count();

                            if (countOfUniqueRelatedBid == 1) {

                                List<AwardEntity> awardsWithStatusDisqualified = awards
                                        .stream()
                                        .filter(award -> DISQUALIFIED.equalsIgnoreCase(award.getStatus()))
                                        .collect(toList());

                                if (!isEmpty(awardsWithStatusDisqualified)) {
                                    for (AwardEntity award : awardsWithStatusDisqualified) {
                                        boolean exist = tender.getLots()
                                                .stream()
                                                .anyMatch(lot -> award.getLot().getId().equals(lot.getId()) && (
                                                        ACTIVE.equalsIgnoreCase(lot.getStatus()) || COMPLETE.equalsIgnoreCase(lot.getStatus()))
                                                );
                                        if (exist) {
                                            indicator.setIndicatorValue(PASSED);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                indicatorDAOService.save(indicator);
            }
        }
        log.info("Finish handle KRAI 30 indicator");
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

    private boolean awardsMoreThan4(List<AwardEntity> awards) {
        return awards.size() > 4;
    }
}
