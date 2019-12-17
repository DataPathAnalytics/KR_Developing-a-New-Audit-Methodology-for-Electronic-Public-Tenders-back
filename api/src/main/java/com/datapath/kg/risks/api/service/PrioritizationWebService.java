package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.request.BuyerPrioritizationRequest;
import com.datapath.kg.risks.api.request.TenderPrioritizationRequest;
import com.datapath.kg.risks.api.response.PrioritizationBuyersResponse;
import com.datapath.kg.risks.api.response.PrioritizationTendersResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface PrioritizationWebService {

    PrioritizationTendersResponse getTenders(TenderPrioritizationRequest request);

    PrioritizationBuyersResponse getBuyers(BuyerPrioritizationRequest request);

    ResponseEntity getTendersFile(TenderPrioritizationRequest request) throws IOException;

    ResponseEntity getBuyersFile(BuyerPrioritizationRequest request) throws IOException;
}
