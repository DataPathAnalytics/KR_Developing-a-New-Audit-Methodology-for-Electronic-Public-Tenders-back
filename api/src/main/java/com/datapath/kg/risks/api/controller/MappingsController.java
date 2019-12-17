package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.dto.MappingsDTO;
import com.datapath.kg.risks.api.service.MappingsWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("mappings")
public class MappingsController {

    @Autowired
    private MappingsWebService service;

    @GetMapping
    public MappingsDTO getMappings() {
        return service.getMappings();
    }
}
