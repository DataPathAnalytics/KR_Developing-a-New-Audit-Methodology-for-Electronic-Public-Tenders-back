package com.datapath.kg.risks.loader.handlers;

import com.datapath.kg.risks.loader.dao.entity.RelatedProcessEntity;
import com.datapath.kg.risks.loader.dao.entity.TenderEntity;
import com.datapath.kg.risks.loader.dao.entity.indicators.TenderIndicatorEntity;
import com.datapath.kg.risks.loader.dao.service.TenderDAOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.datapath.kg.risks.loader.utils.Constants.*;
import static com.datapath.kg.risks.loader.utils.Indicator.KRAI5;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@Slf4j
public class KRAI5Handler extends KRAIBaseHandler {

    private static final List<String> METHOD_DETAILS = Arrays.asList(ONE_STAGE, DOWNGRADE, SIMPLICATED);

    @Autowired
    private TenderDAOService service;

    @Override
    @Transactional
    public void handle() {
        log.info("Start handle KRAI 5 indicator");

        List<Integer> tenderIds = service.getKRAI5AcceptableTenders();

        for (Integer tenderId : tenderIds) {
            log.info("Process {} of {} tenders", tenderIds.indexOf(tenderId), tenderIds.size());
            TenderEntity tender = service.getTender(tenderId);
            TenderIndicatorEntity indicator = buildTenderIndicator(tender, KRAI5);
            if (!tender.isBadQuality()) {
                if (isEmpty(tender.getRelatedProcesses())) {
                    indicator.setIndicatorValue(PASSED);
                } else {
                    RelatedProcessEntity relatedProcess = tender.getRelatedProcesses().iterator().next();
                    if (nonNull(relatedProcess.getIdentifier()) && PRIOR.equalsIgnoreCase(relatedProcess.getRelationship())) {
                        Optional<TenderEntity> tenderByIdentifier = service.findById(relatedProcess.getIdentifier());
                        if (!tenderByIdentifier.isPresent()) {
                            indicator.setIndicatorValue(PASSED);
                        } else {
                            TenderEntity previousTender = tenderByIdentifier.get();
                            if (!CONTRACT_SIGNED.equalsIgnoreCase(previousTender.getCurrentStage())
                                    || !METHOD_DETAILS.contains(previousTender.getProcurementMethodDetails())) {
                                indicator.setIndicatorValue(PASSED);
                            }
                        }
                    } else {
                        indicator.setIndicatorValue(PASSED);
                    }
                }
            }
            indicatorDAOService.save(indicator);
        }
        log.info("Finish handle KRAI 5 indicator");
    }
}