package com.datapath.kg.risks.api.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddAuditorTemplateRequest {

    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private Integer typeId;

    @NotNull
    @NotEmpty
    private List<Category> categories;

    @Data
    public static class Category {
        @NotNull
        private String name;
        private String number;
        @NotNull
        @NotEmpty
        private List<Question> questions;
    }

    @Data
    public static class Question {
        @NotNull
        @NotEmpty
        private String description;
        private String number;
    }

}