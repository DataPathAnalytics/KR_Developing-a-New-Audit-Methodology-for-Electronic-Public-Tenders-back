package com.datapath.kg.risks.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuestionCategoryDTO {

    @NotNull
    private Integer id;
    @NotNull
    @NotEmpty
    private String name;
    private String number;

    private List<QuestionDTO> questions = new ArrayList<>();

}