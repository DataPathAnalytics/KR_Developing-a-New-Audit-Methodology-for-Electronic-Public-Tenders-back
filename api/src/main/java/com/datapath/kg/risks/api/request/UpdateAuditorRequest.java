package com.datapath.kg.risks.api.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAuditorRequest {

    @NotNull
    private Integer id;
    private String name;
    @NotNull
    @NotEmpty
    private String email;

    private boolean disabled;
}
