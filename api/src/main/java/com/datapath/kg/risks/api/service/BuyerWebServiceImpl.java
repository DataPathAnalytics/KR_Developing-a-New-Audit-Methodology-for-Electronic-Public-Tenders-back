package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.entity.BuyerEntity;
import com.datapath.kg.risks.api.dao.service.BuyerDAOService;
import com.datapath.kg.risks.api.dto.BuyerDTO;
import com.datapath.kg.risks.api.response.BuyersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuyerWebServiceImpl implements BuyerWebService {

    @Autowired
    private BuyerDAOService dao;
    @Autowired
    private DTOEntityMapper mapper;

    @Override
    public BuyersResponse search(String value) {
        List<BuyerEntity> entities = dao.search(value);
        List<BuyerDTO> buyers = entities.stream().map(mapper::map).collect(Collectors.toList());
        BuyersResponse response = new BuyersResponse();
        response.setBuyers(buyers);
        return response;
    }
}
