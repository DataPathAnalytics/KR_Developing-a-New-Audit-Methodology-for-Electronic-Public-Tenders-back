package com.datapath.kg.risks.api.dto.dashboard.tender;

import lombok.Data;

@Data
public class TenderTopDTO {
    private Long tenderId;
    private Long result;
    private String link;
    private String code;
    private String datePublished;
}
