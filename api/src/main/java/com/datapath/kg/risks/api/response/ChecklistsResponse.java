package com.datapath.kg.risks.api.response;

import com.datapath.kg.risks.api.dto.ChecklistDTO;
import lombok.Data;

import java.util.List;

@Data
public class ChecklistsResponse {

    private List<ChecklistDTO> checklists;

}
