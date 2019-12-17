package com.datapath.kg.risks.loader.dao.service;

import com.datapath.kg.risks.loader.dao.entity.AwardEntity;
import com.datapath.kg.risks.loader.dao.repository.AwardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AwardDAOService {

    @Autowired
    private AwardRepository repository;

    public List<AwardEntity> getByTenderId(Integer tenderId) {
        return repository.findByTenderId(tenderId);
    }
}
