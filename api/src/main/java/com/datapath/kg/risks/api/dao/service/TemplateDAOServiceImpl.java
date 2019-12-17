package com.datapath.kg.risks.api.dao.service;

import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.TemplateEntity;
import com.datapath.kg.risks.api.dao.entity.TemplateTypeEntity;
import com.datapath.kg.risks.api.dao.repository.TemplateRepository;
import com.datapath.kg.risks.api.dao.repository.TemplateTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TemplateDAOServiceImpl implements TemplateDAOService {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private TemplateTypeRepository templateTypeRepository;

    @Override
    public List<TemplateEntity> getTemplates(AuditorEntity auditor) {
        return templateRepository.findByBaseTrueOrAuditor(auditor);
    }

    @Override
    public Optional<TemplateEntity> getTemplate(Integer id) {
        return templateRepository.findById(id);
    }

    @Override
    public TemplateEntity getTemplateById(Integer id) {
        return templateRepository.getOne(id);
    }

    @Override
    public TemplateEntity saveTemplate(TemplateEntity entity) {
        return templateRepository.save(entity);
    }

    @Override
    public void deleteTemplate(Integer id) {
        templateRepository.deleteById(id);
    }

    @Override
    public List<TemplateTypeEntity> getTypes() {
        return templateTypeRepository.findAll();
    }

    @Override
    public TemplateTypeEntity getType(Integer typeId) {
        return templateTypeRepository.getOne(typeId);
    }

}
