package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.dto.TemplateDTO;
import com.datapath.kg.risks.api.request.AddAuditorTemplateRequest;
import com.datapath.kg.risks.api.request.AddBaseTemplateRequest;
import com.datapath.kg.risks.api.response.TemplateTypesResponse;
import com.datapath.kg.risks.api.response.TemplatesResponse;

public interface TemplateService {

    TemplatesResponse getTemplates();

    TemplateDTO getTemplate(Integer id);

    TemplateDTO addBaseTemplate(AddBaseTemplateRequest request);

    TemplateDTO addAuditorTemplate(AddAuditorTemplateRequest request);

    TemplateDTO updateTemplate(TemplateDTO dto);

    void deleteTemplate(Integer id);

    TemplateTypesResponse getTypes();

}
