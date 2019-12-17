package com.datapath.kg.risks.api.controller;


import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;
import com.datapath.kg.risks.api.response.PrioritizationBuyersResponse;
import com.datapath.kg.risks.api.response.PrioritizationTendersResponse;
import com.datapath.kg.risks.api.service.PrioritizationWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("prioritization")
public class PrioritizationController {

    @Autowired
    private PrioritizationWebService service;

    @PostMapping("tenders")
    public PrioritizationTendersResponse getTenders(@RequestBody TenderPrioritizationRequest request) {
        return service.getTenders(request);
    }

    @PostMapping("buyers")
    public PrioritizationBuyersResponse getBuyers(@RequestBody BuyerPrioritizationRequest request) {
        return service.getBuyers(request);
    }

    @PostMapping("tenders/export")
    public ResponseEntity getTendersFile(@RequestBody TenderPrioritizationRequest request) throws IOException {
        return service.getTendersFile(request);
    }

    @PostMapping("buyers/export")
    public ResponseEntity getBuyersFile(@RequestBody BuyerPrioritizationRequest request) throws IOException {
        return service.getBuyersFile(request);
    }

}