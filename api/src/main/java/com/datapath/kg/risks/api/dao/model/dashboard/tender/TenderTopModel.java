package com.datapath.kg.risks.api.dao.model.dashboard.tender;

import lombok.Data;

@Data
public class TenderTopModel {
    private Long tenderId;
    private Long result;
    private String code;
    private String datePublished;
}
