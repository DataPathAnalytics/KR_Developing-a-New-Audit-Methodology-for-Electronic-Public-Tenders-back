package com.datapath.kg.risks.api.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AddQuestionCategoryRequest {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Integer templateId;

    private String number;

}

