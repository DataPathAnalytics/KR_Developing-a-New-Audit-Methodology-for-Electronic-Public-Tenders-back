package com.datapath.kg.risks.api.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordRequest {

    @NotNull
    @NotEmpty
    private String token;

    @NotNull
    @NotEmpty
    private String password;
}
