package com.datapath.kg.risks.api.request;

import lombok.Data;

import java.util.List;

@Data
public class ChecklistRequest {

    private Integer status;
    private Integer templateType;
    private boolean onlyMyChecklists;
    private List<Integer> ids;
    private List<Integer> auditorIds;
}
