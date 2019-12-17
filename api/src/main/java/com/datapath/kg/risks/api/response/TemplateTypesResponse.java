package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.TemplateTypeDTO;
import lombok.Data;

import java.util.List;

@Data
public class TemplateTypesResponse {

    private List<TemplateTypeDTO> types;

}
