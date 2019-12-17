package com.datapath.kg.risks.loader.dao.service;

import com.datapath.kg.risks.loader.dao.entity.indicators.TenderIndicatorEntity;
import com.datapath.kg.risks.loader.dao.repository.IndicatorRepository;
import com.datapath.kg.risks.loader.dao.repository.TenderIndicatorRepository;
import org.springframework.stereotype.Component;

@Component
public class IndicatorDAOService {

    private TenderIndicatorRepository tenderIndicatorRepository;

    public IndicatorDAOService(TenderIndicatorRepository tenderIndicatorRepository) {
        this.tenderIndicatorRepository = tenderIndicatorRepository;
    }

    public TenderIndicatorEntity save(TenderIndicatorEntity entity) {
        return tenderIndicatorRepository.save(entity);
    }

    public void delete(Integer tenderId, Integer indicatorId) {
        tenderIndicatorRepository.delete(tenderId, indicatorId);
    }

}