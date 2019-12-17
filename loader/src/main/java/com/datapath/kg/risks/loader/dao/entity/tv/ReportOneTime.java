package com.datapath.kg.risks.loader.dao.entity.tv;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportOneTime {

    private Integer buyerId;
    private String cpv;
    private LocalDateTime date;
    private Integer publishedYear;
}
