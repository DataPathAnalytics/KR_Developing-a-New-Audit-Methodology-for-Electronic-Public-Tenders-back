package com.datapath.kg.risks.loader.dao.service;

import com.datapath.kg.risks.loader.dao.entity.ContactPointEntity;
import com.datapath.kg.risks.loader.dao.repository.ContactPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactPointDAOService {

    @Autowired
    private ContactPointRepository repository;

    public void deleteByTenderId(Integer tenderId) {
        repository.deleteAllByTenderId(tenderId);
    }

    public void saveAll(List<ContactPointEntity> contactPointEntities) {
        repository.saveAll(contactPointEntities);
    }
}
