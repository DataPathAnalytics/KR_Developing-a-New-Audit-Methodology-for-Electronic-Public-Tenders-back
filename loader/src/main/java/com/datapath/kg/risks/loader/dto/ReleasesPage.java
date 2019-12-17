package com.datapath.kg.risks.loader.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReleasesPage {

    private List<ReleaseDTO> content;
    private boolean last;

}
