package com.datapath.kg.risks.loader.dto;

import lombok.Data;

import java.util.List;

@Data
public class BidsDTO {

    private List<BidDetailDTO> details;
}
