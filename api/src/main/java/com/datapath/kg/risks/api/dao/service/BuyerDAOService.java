package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.BuyerEntity;

import java.util.List;

public interface BuyerDAOService {

    List<BuyerEntity> search(String value);

    BuyerEntity findById(Integer id);

}
