package com.datapath.kg.risks.loader.dto;

import lombok.Data;

@Data
public class ItemDTO {

    private Integer id;
    private ClassificationDTO classification;
    private Integer relatedLot;
    private Double quantity;
    private UnitDTO unit;
}
