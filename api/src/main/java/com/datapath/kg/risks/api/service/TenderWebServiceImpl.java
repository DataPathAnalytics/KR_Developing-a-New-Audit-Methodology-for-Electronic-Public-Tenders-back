package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.EntityConverter;
import com.datapath.kg.risks.api.dao.entity.TenderIndicatorEntity;
import com.datapath.kg.risks.api.dao.service.TenderDAOService;
import com.datapath.kg.risks.api.dto.TenderIndicatorDTO;
import com.datapath.kg.risks.api.response.TenderIndicatorsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TenderWebServiceImpl implements TenderWebService {

    @Autowired
    private TenderDAOService daoService;
    @Autowired
    private EntityConverter converter;

    @Override
    public TenderIndicatorsResponse getIndicators(Integer tenderId) {
        TenderIndicatorsResponse response = new TenderIndicatorsResponse();

        List<TenderIndicatorEntity> entities = daoService.getIndicators(tenderId);
        List<TenderIndicatorDTO> indicators = entities.stream().map(converter::convert).collect(Collectors.toList());
        response.setIndicators(indicators);

        return response;
    }

}
