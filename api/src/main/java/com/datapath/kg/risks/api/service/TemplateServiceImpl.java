package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.entity.AuditorEntity;
import com.datapath.kg.risks.api.dao.entity.TemplateEntity;
import com.datapath.kg.risks.api.dao.entity.TemplateTypeEntity;
import com.datapath.kg.risks.api.dao.service.AuditorDAOService;
import com.datapath.kg.risks.api.dao.service.TemplateDAOService;
import com.datapath.kg.risks.api.dto.TemplateDTO;
import com.datapath.kg.risks.api.dto.TemplateTypeDTO;
import com.datapath.kg.risks.api.request.AddAuditorTemplateRequest;
import com.datapath.kg.risks.api.request.AddBaseTemplateRequest;
import com.datapath.kg.risks.api.response.TemplateTypesResponse;
import com.datapath.kg.risks.api.response.TemplatesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Component
public class TemplateServiceImpl implements TemplateService {

    @Autowired
    private DTOEntityMapper mapper;
    @Autowired
    private TemplateDAOService templateDAO;
    @Autowired
    private AuditorDAOService auditorDAO;

    @Override
    public TemplatesResponse getTemplates() {
        AuditorEntity auditor = auditorDAO.getCurrent();

        TemplatesResponse response = new TemplatesResponse();
        List<TemplateDTO> templates = templateDAO.getTemplates(auditor).stream().map(entity -> mapper.map(entity)).collect(toList());
        response.setTemplates(templates);
        return response;
    }

    @Override
    public TemplateDTO getTemplate(Integer id) {
        Optional<TemplateEntity> template = templateDAO.getTemplate(id);
        return template.map(templateEntity -> mapper.map(templateEntity)).orElse(null);
    }

    @Override
    public TemplateDTO addBaseTemplate(AddBaseTemplateRequest request) {
        AuditorEntity auditor = auditorDAO.getCurrent();
        TemplateTypeEntity type = templateDAO.getType(request.getTypeId());

        TemplateEntity entity = mapper.map(request);
        entity.setBase(true);
        entity.setType(type);
        entity.setAuditor(auditor);

        return mapper.map(templateDAO.saveTemplate(entity));
    }

    @Override
    public TemplateDTO addAuditorTemplate(AddAuditorTemplateRequest request) {
        TemplateTypeEntity type = templateDAO.getType(request.getTypeId());
        TemplateEntity entity = mapper.map(request);
        entity.setType(type);
        entity.setBase(false);
        entity.setAuditor(auditorDAO.getCurrent());
        TemplateEntity template = templateDAO.saveTemplate(entity);
        return mapper.map(template);
    }

    @Override
    public TemplateDTO updateTemplate(TemplateDTO dto) {
        TemplateEntity entity = templateDAO.getTemplateById(dto.getId());
        entity.setBase(dto.isBase());
        entity.setName(dto.getName());
        entity.setType(templateDAO.getType(dto.getType().getId()));
        return mapper.map(templateDAO.saveTemplate(entity));
    }

    @Override
    public void deleteTemplate(Integer id) {
        templateDAO.deleteTemplate(id);
    }

    @Override
    public TemplateTypesResponse getTypes() {
        TemplateTypesResponse response = new TemplateTypesResponse();
        List<TemplateTypeDTO> types = templateDAO.getTypes().stream().map(entity -> mapper.map(entity)).collect(toList());
        response.setTypes(types);
        return response;
    }

}