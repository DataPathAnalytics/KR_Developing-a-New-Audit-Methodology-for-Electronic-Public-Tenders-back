package com.datapath.kg.risks.api.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;

}
