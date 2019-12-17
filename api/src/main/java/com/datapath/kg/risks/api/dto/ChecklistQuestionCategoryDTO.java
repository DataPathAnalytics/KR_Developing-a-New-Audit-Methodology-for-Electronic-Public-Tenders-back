package com.datapath.kg.risks.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ChecklistQuestionCategoryDTO {

    @NotNull
    private Integer id;
    @NotNull
    @NotEmpty
    private String name;

}