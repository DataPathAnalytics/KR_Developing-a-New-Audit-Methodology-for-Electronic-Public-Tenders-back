package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.repository.AuditorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.datapath.kg.risks.api.Utils.getUserEmail;

@Service
public class AuditorDAOServiceImpl implements AuditorDAOService {

    @Autowired
    private AuditorRepository repository;

    @Override
    public AuditorEntity getCurrent() {
        return repository.findByEmail(getUserEmail());
    }

    @Override
    public AuditorEntity getByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<AuditorEntity> searchByAuditorName(String value) {
        return repository.searchByAuditorName(value);
    }

    @Override
    public List<AuditorEntity> searchByAuditorNameForAdmin(String value) {
        return repository.searchByAuditorNameForAdmin(value);
    }
}
