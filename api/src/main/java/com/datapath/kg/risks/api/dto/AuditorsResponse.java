package com.datapath.kg.risks.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuditorsResponse {

    private List<AuditorDTO> auditors;

}
