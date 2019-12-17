package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.WorkDateEntity;

import java.util.List;

public interface WorkDateDAOService {

    List<WorkDateEntity> getByYear(Integer year);

    void save(List<WorkDateEntity> dates);
}
