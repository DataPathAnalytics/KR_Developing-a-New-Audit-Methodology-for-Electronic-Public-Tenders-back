package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.AuditNameDTO;
import lombok.Data;

import java.util.List;

@Data
public class AuditNameResponse {

    private List<AuditNameDTO> auditNameDetails;
}
