package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;

import java.util.List;

public interface AuditorDAOService {

    AuditorEntity getCurrent();

    AuditorEntity getByEmail(String email);

    List<AuditorEntity> searchByAuditorName(String value);

    List<AuditorEntity> searchByAuditorNameForAdmin(String value);
}
