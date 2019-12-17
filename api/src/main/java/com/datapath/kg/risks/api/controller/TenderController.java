package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.response.TenderIndicatorsResponse;
import com.datapath.kg.risks.api.service.TenderWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tenders")
public class TenderController {

    @Autowired
    private TenderWebService service;

    @GetMapping("{tenderId}/indicators")
    public TenderIndicatorsResponse getIndicators(@PathVariable Integer tenderId) {
        return service.getIndicators(tenderId);
    }
}