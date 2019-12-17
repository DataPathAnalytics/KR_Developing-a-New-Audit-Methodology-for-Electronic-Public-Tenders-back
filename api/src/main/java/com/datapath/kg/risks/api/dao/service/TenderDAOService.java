package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.TenderEntity;
import com.datapath.kg.risks.api.dao.entity.TenderIndicatorEntity;
import java.util.List;

public interface TenderDAOService {

    TenderEntity getTender(Integer id);

    List<TenderIndicatorEntity> getIndicators(Integer tenderId);

}