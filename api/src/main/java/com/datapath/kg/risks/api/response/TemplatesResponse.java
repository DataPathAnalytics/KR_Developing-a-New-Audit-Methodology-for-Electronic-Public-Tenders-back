package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.TemplateDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TemplatesResponse {

    private List<TemplateDTO> templates = new ArrayList<>();

}
