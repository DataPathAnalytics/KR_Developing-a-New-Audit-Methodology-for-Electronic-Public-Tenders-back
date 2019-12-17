package com.datapath.kg.risks.api.dto;

import lombok.Data;

@Data
public class QuestionDTO {

    private Integer id;
    private String description;
    private boolean base;
    private String number;

}