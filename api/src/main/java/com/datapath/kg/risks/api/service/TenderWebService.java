package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.response.TenderIndicatorsResponse;

public interface TenderWebService {

    TenderIndicatorsResponse getIndicators(Integer tenderId);
}
