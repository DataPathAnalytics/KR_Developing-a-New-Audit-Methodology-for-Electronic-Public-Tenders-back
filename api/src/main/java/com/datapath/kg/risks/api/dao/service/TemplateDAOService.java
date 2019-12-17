package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.TemplateEntity;
import com.datapath.kg.risks.api.dao.entity.TemplateTypeEntity;

import java.util.List;
import java.util.Optional;

public interface TemplateDAOService {

    List<TemplateEntity> getTemplates(AuditorEntity auditor);

    Optional<TemplateEntity> getTemplate(Integer id);

    TemplateEntity getTemplateById(Integer id);

    TemplateEntity saveTemplate(TemplateEntity entity);

    void deleteTemplate(Integer id);

    List<TemplateTypeEntity> getTypes();

    TemplateTypeEntity getType(Integer typeId);
}
