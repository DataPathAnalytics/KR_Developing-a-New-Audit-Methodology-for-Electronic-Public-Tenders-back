package com.datapath.kg.risks.api.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddQuestionRequest {

    @NotNull
    private String description;
    @NotNull
    private Integer categoryId;
    private String number;
    private boolean base;

}