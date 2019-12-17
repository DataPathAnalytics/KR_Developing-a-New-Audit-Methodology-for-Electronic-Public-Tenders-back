package com.datapath.kg.risks.loader.dto;

import lombok.Data;

import java.util.List;

@Data
public class ContractDTO {

    private Integer id;
    private String dateSigned;
    private String status;
    private List<Long> awardIDs;
    private ValueDTO value;
}
