package com.datapath.kg.risks.loader.dto;

import lombok.Data;

import java.util.List;

@Data
public class AwardDTO {

    private Integer id;
    private String datePublished;
    private String status;
    private Integer relatedBid;
    private List<Integer> relatedLots;
    private ValueDTO value;
}