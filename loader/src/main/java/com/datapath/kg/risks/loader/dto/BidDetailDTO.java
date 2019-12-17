package com.datapath.kg.risks.loader.dto;

import lombok.Data;

import java.util.List;

@Data
public class BidDetailDTO {
    private Integer id;
    private String status;
    private List<LotDTO> relatedLots;
    private List<PriceProposalDTO> priceProposals;
    private List<TendererDTO> tenderers;
    private String dateDisclosed;
}
