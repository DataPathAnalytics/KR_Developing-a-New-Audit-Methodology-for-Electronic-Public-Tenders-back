package com.datapath.kg.risks.loader.dto;

import lombok.Data;

@Data
public class LotDTO {

    private Integer id;
    private String status;
    private ValueDTO value;
    private String number;
}
