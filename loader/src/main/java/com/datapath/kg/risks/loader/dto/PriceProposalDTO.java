package com.datapath.kg.risks.loader.dto;

import lombok.Data;

@Data
public class PriceProposalDTO {
    private Integer id;
    private Integer relatedItem;
    private Integer relatedLot;
    private UnitDTO unit;
}
