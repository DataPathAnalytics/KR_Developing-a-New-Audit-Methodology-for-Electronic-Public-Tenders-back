package com.datapath.kg.risks.loader.dao.service;

import com.datapath.kg.risks.loader.dao.entity.PartyEntity;
import com.datapath.kg.risks.loader.dao.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyDAOService {

    @Autowired
    private PartyRepository repository;

    public Integer save(PartyEntity partyEntity) {
        repository.findByOuterId(partyEntity.getOuterId()).ifPresent(party -> partyEntity.setId(party.getId()));
        return repository.save(partyEntity).getId();
    }

    public List<PartyEntity> findByIdentifierOrLegalName(String value) {
        return repository.findbyIdentifierOrLegalName(value);
    }


}