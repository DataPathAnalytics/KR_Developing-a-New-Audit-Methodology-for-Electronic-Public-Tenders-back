package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.OkgzEntity;
import com.datapath.kg.risks.api.dao.repository.OkgzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OkgzDAOServiceImpl implements OkgzDAOService {

    @Autowired
    private OkgzRepository repository;

    public List<OkgzEntity> getAll() {
        return repository.findAll();
    }
}
