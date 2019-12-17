package com.datapath.kg.risks.loader.handlers;

import com.datapath.kg.risks.loader.dao.entity.AwardEntity;
import com.datapath.kg.risks.loader.dao.entity.TenderEntity;
import com.datapath.kg.risks.loader.dao.entity.indicators.TenderIndicatorEntity;
import com.datapath.kg.risks.loader.dao.service.IndicatorDAOService;
import com.datapath.kg.risks.loader.dao.service.VariablesDAOService;
import com.datapath.kg.risks.loader.utils.Indicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static com.datapath.kg.risks.loader.utils.Constants.FAILED;
import static com.datapath.kg.risks.loader.utils.Constants.INCORRECT;
import static java.util.Objects.nonNull;

public abstract class KRAIBaseHandler  implements KRAIHandler {

    IndicatorDAOService indicatorDAOService;
    VariablesDAOService variablesDAOService;

    @Autowired
    public void setIndicatorDAOService(IndicatorDAOService indicatorDAOService) {
        this.indicatorDAOService = indicatorDAOService;
    }

    @Autowired
    public void setVariablesDAOService(VariablesDAOService variablesDAOService) {
        this.variablesDAOService = variablesDAOService;
    }

    boolean hasAwardOldestThan30Days(TenderEntity tender) {
        if (CollectionUtils.isEmpty(tender.getAwards())) return false;
        LocalDateTime min = tender.getAwards()
                .stream()
                .filter(a -> nonNull(a.getDate()))
                .map(AwardEntity::getDate)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        return nonNull(min) && Duration.between(min, ZonedDateTime.now()).toDays() > 30;
    }

    TenderIndicatorEntity buildTenderIndicator(TenderEntity tender, Indicator indicator) {
        TenderIndicatorEntity tenderIndicator = new TenderIndicatorEntity();
        tenderIndicator.setTenderId(tender.getId());
        tenderIndicator.setIndicatorId(indicator.getId());
        tenderIndicator.setIndicatorValue(tender.isBadQuality() ? INCORRECT : FAILED);
        return tenderIndicator;
    }

}
