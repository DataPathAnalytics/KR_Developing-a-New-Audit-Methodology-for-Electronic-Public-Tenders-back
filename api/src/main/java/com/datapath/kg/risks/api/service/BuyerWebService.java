package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.response.BuyersResponse;

public interface BuyerWebService {

    BuyersResponse search(String value);

}