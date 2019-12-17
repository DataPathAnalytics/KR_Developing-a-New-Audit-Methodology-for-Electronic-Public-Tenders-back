package com.datapath.kg.risks.api.export.utils;

import com.itextpdf.text.BaseColor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CellSettings {

    private BaseColor backgroundColor;
    private BaseColor borderColor;
    private Integer border;
    private Integer verticalAlignment;
    private Integer horizontalAlignment;
    private Integer colspan;
    private Integer paddingLeft;
}
