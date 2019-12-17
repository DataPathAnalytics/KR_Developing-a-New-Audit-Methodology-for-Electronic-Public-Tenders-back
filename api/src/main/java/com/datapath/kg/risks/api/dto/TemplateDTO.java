package com.datapath.kg.risks.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class TemplateDTO {

    @NotNull
    private Integer id;
    @NotNull
    @NotEmpty
    private String name;
    private boolean base;
    private TemplateTypeDTO type;
    private LocalDate modifiedDate;

    private List<QuestionCategoryDTO> categories;

}