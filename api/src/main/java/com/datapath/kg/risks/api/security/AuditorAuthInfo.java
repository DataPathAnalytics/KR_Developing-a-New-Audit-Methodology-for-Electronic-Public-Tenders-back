package com.datapath.kg.risks.api.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuditorAuthInfo {
    private String id;
    private String email;
}
