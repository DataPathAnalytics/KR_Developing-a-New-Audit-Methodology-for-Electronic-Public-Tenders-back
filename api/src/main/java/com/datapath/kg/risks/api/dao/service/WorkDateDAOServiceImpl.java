package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.WorkDateEntity;
import com.datapath.kg.risks.api.dao.repository.WorkDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkDateDAOServiceImpl implements WorkDateDAOService {

    @Autowired
    private WorkDateRepository repository;

    @Override
    public List<WorkDateEntity> getByYear(Integer year) {
        return repository.findByYear(year);
    }

    @Override
    public void save(List<WorkDateEntity> dates) {
        repository.saveAll(dates);
    }
}
