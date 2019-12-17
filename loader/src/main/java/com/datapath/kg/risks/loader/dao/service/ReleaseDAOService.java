package com.datapath.kg.risks.loader.dao.service;

import com.datapath.kg.risks.loader.dao.entity.ReleaseEntity;
import com.datapath.kg.risks.loader.dao.repository.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReleaseDAOService {

    @Autowired
    private ReleaseRepository releaseRepository;

    public ReleaseEntity getLastRelease() {
        return releaseRepository.findFirstByDateNotNullOrderByDateDesc();
    }

}