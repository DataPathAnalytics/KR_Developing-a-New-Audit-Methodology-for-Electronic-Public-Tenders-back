package com.datapath.kg.risks.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TendersScoreRequest {
    private Integer buyerId;
    private LocalDate startPeriodDate;
    private LocalDate endPeriodDate;
}
