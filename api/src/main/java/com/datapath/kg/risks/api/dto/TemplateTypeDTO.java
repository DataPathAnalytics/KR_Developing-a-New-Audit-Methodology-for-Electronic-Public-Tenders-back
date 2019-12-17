package com.datapath.kg.risks.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class TemplateTypeDTO {

    private Integer id;
    private String name;
    private String nameEn;
    private List<String> procurementMethods;

}