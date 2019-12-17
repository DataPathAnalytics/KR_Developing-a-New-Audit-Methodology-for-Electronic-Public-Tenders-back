package com.datapath.kg.risks.api.response.dashboard.base;

import com.datapath.kg.risks.api.dto.dashboard.base.BaseTopDTO;
import lombok.Data;

import java.util.List;

@Data
public class BaseTopResponse {
    private List<BaseTopDTO> data;
}
