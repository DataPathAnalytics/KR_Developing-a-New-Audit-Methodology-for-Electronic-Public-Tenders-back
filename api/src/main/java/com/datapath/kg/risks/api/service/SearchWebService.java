package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.response.AuditNameResponse;
import com.datapath.kg.risks.api.response.AuditorNameResponse;
import com.datapath.kg.risks.api.response.BuyersResponse;

public interface SearchWebService {

    BuyersResponse searchBuyer(String value);

    AuditNameResponse searchAuditName(String value);

    AuditorNameResponse searchAuditorName(String value);
}
