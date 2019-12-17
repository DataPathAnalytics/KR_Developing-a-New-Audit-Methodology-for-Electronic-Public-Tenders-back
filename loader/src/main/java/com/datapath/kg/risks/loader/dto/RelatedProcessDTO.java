package com.datapath.kg.risks.loader.dto;

import lombok.Data;

import java.util.List;

@Data
public class RelatedProcessDTO {

    private Integer identifier;
    private List<String> relationship;
}
