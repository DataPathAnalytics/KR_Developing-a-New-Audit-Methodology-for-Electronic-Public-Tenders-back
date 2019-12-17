package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.AuditorNameDTO;
import lombok.Data;

import java.util.List;

@Data
public class AuditorNameResponse {
    private List<AuditorNameDTO> auditorNameDetails;
}
