package com.datapath.kg.risks.api;

import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import com.datapath.kg.risks.api.dao.entity.TenderIndicatorEntity;
import com.datapath.kg.risks.api.dto.ChecklistDTO;
import com.datapath.kg.risks.api.dto.TenderIndicatorDTO;
import com.datapath.kg.risks.api.request.SaveChecklistRequest;

public interface EntityConverter {

    ChecklistEntity convert(SaveChecklistRequest request);

    TenderIndicatorDTO convert(TenderIndicatorEntity entity);

    ChecklistDTO convert(ChecklistEntity entity);
}