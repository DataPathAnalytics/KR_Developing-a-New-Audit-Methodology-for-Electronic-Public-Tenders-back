package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.dto.TemplateDTO;
import com.datapath.kg.risks.api.request.AddAuditorTemplateRequest;
import com.datapath.kg.risks.api.request.AddBaseTemplateRequest;
import com.datapath.kg.risks.api.response.TemplateTypesResponse;
import com.datapath.kg.risks.api.response.TemplatesResponse;
import com.datapath.kg.risks.api.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/templates")
public class TemplateController {

    @Autowired
    private TemplateService service;

    @GetMapping
    public TemplatesResponse getTemplates() {
        return service.getTemplates();
    }

    @GetMapping("{id}")
    public TemplateDTO getTemplate(@PathVariable Integer id) {
        return service.getTemplate(id);
    }

    @PostMapping("auditor")
    public TemplateDTO addAuditorTemplate(@Valid @RequestBody AddAuditorTemplateRequest request) {
        return service.addAuditorTemplate(request);
    }

    @PostMapping("base")
    public TemplateDTO addBaseTemplate(@Valid @RequestBody AddBaseTemplateRequest request) {
        return service.addBaseTemplate(request);
    }

    @PutMapping
    public TemplateDTO updateTemplate(@Valid @RequestBody TemplateDTO dto) {
        return service.updateTemplate(dto);
    }

    @DeleteMapping("{id}")
    public void deleteTemplate(@PathVariable Integer id) {
        service.deleteTemplate(id);
    }

    @GetMapping("types")
    public TemplateTypesResponse getTypes() {
        return service.getTypes();
    }

}