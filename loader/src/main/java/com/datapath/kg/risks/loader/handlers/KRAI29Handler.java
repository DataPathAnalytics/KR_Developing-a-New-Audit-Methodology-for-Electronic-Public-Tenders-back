package com.datapath.kg.risks.loader.handlers;

import com.datapath.kg.risks.loader.dao.entity.AwardEntity;
import com.datapath.kg.risks.loader.dao.entity.LotEntity;
import com.datapath.kg.risks.loader.dao.entity.TenderEntity;
import com.datapath.kg.risks.loader.dao.entity.indicators.TenderIndicatorEntity;
import com.datapath.kg.risks.loader.dao.repository.BidRepository;
import com.datapath.kg.risks.loader.dao.service.BidLotDAOService;
import com.datapath.kg.risks.loader.dao.service.TenderDAOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.datapath.kg.risks.loader.utils.Constants.*;
import static com.datapath.kg.risks.loader.utils.Indicator.KRAI29;
import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class KRAI29Handler extends KRAIBaseHandler {

    @Autowired
    private TenderDAOService tenderDAO;

    @Override
    @Transactional
    public void handle() {
        log.info("Start handle KRAI 29 indicator");

        List<Integer> tenders = tenderDAO.getKRAI29AcceptableTenders();
        log.info("Found {} tenders to process", tenders.size());
        for (Integer tenderId : tenders) {

            log.info("Process {} of {} tenders",tenders.indexOf(tenderId), tenders.size());
            TenderEntity tender = tenderDAO.getTender(tenderId);

            if (isAcceptable(tender)) {
                TenderIndicatorEntity indicator = buildTenderIndicator(tender, KRAI29);
                if (!tender.isBadQuality()) {
                    List<LotEntity> lots = tender.getLots()
                            .stream()
                            .filter(lot -> asList(ACTIVE, COMPLETE).contains(lot.getStatus()))
                            .collect(Collectors.toList());

                    for (LotEntity lot : lots) {

                        long bidsCount = tender.getBids()
                                .stream()
                                .filter(bid -> !isEmpty(bid.getBidLots()))
                                .filter(bid -> VALID.equalsIgnoreCase(bid.getStatus()))
                                .flatMap(bid -> bid.getBidLots().stream())
                                .filter(bidLot -> bidLot.getLotId().equals(lot.getId()))
                                .count();

                        long awardsCount = lot.getAwards().stream()
                                .filter(award -> asList(ACTIVE, DISQUALIFIED).contains(award.getStatus()))
                                .count();

                        if (bidsCount >= 4 && awardsCount == bidsCount) {
                            Set<String> statuses = lot.getAwards().stream()
                                    .filter(award -> nonNull(award.getLot()) && nonNull(award.getStatus()))
                                    .filter(award -> asList(ACTIVE, DISQUALIFIED).contains(award.getStatus()))
                                    .map(AwardEntity::getStatus)
                                    .collect(Collectors.toSet());

                            if (statuses.size() == 2 && statuses.containsAll(asList(ACTIVE, DISQUALIFIED))) {
                                indicator.setIndicatorValue(PASSED);
                                break;
                            }
                        }
                    }
                }
                indicatorDAOService.save(indicator);
            }
        }
        log.info("Finish handle KRAI 29 indicator");
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
