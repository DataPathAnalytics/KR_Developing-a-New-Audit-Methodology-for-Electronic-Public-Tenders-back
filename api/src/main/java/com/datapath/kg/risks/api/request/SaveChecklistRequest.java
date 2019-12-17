package com.datapath.kg.risks.api.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class SaveChecklistRequest {

    private Integer id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private String templateName;
    private Integer templateTypeId;
    private Integer buyerId;
    private Integer tenderId;
    private Integer manualScoreId;
    private Integer manualTendersScoreId;
    private String tendersComment;
    private Integer tendersImpactId;
    @NotNull
    private Integer statusId;

    private String auditName;
    private String summary;
    private String contractNumber;
    private Double contractAmount;
    private String contractDescription;
    private String supplier;
    @NotNull
    @NotEmpty
    private List<Answer> answers;
    private List<Indicator> indicators;

    @Data
    public static class Answer {
        private Integer answerTypeId;
        private Integer componentImpactId;
        private String comment;
        private String npa;
        private String categoryNumber;
        private String categoryName;
        private String questionNumber;
        private String questionDescription;
        private boolean baseQuestion;
    }

    @Data
    public static class Indicator {
        private Integer id;
        private Integer indicatorId;
        private Integer answerTypeId;
        private Integer componentImpactId;
        private String comment;
    }

}