package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.response.AuditNameResponse;
import com.datapath.kg.risks.api.response.AuditorNameResponse;
import com.datapath.kg.risks.api.response.BuyersResponse;
import com.datapath.kg.risks.api.service.SearchWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchWebService service;

    @GetMapping("buyer")
    public BuyersResponse search(@RequestParam String value) {
        return service.searchBuyer(value);
    }

    @GetMapping("audit-name")
    public AuditNameResponse searchAuditName(@RequestParam String value) {
        return service.searchAuditName(value);
    }

    @GetMapping("auditor-name")
    public AuditorNameResponse searchAuditorName(@RequestParam String value) {
        return service.searchAuditorName(value);
    }
}
