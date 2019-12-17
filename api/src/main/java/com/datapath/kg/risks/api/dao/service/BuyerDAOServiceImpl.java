package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.BuyerEntity;
import com.datapath.kg.risks.api.dao.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BuyerDAOServiceImpl implements BuyerDAOService {

    @Autowired
    private PartyRepository partyRepository;

    @Override
    public List<BuyerEntity> search(String value) {
        return partyRepository.findByIdentifierOrLegalName(value);
    }

    @Override
    public BuyerEntity findById(Integer id) {
        return partyRepository.getOne(id);
    }

}