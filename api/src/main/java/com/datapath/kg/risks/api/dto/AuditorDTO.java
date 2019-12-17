package com.datapath.kg.risks.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AuditorDTO {

    @NotNull
    private Integer id;
    private String name;
    @NotNull
    @NotEmpty
    private String email;
    private boolean disabled = true;
    private List<Integer> permissions;

}