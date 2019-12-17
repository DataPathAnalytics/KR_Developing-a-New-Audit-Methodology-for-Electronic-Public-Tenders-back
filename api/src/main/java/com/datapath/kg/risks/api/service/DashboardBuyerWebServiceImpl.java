package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.model.dashboard.PrioritizationInfoModel;
import com.datapath.kg.risks.api.dao.service.DashboardBuyerDAOService;
import com.datapath.kg.risks.api.dto.dashboard.InfoDTO;
import com.datapath.kg.risks.api.request.DashboardBuyerRequest;
import com.datapath.kg.risks.api.response.dashboard.buyer.BuyerTopByIndicatorCountResponse;
import com.datapath.kg.risks.api.response.dashboard.buyer.BuyerTopResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class DashboardBuyerWebServiceImpl implements DashboardBuyerWebService {

    @Autowired
    private DashboardBuyerDAOService service;
    @Autowired
    private DTOEntityMapper mapper;

    private DecimalFormat decimalFormat = new DecimalFormat("##.0");

    @Override
    public InfoDTO getInfo(DashboardBuyerRequest request) {
        PrioritizationInfoModel prioritizationInfo = service.getPrioritizationInfo(request);

        InfoDTO baseInfo = new InfoDTO();
        baseInfo.setTendersCount(prioritizationInfo.getTendersCount());
        baseInfo.setTendersAmount(prioritizationInfo.getTendersAmount());
        baseInfo.setRiskTendersCount(prioritizationInfo.getRiskTendersCount());
        baseInfo.setRiskTendersAmount(prioritizationInfo.getRiskTendersAmount());
        baseInfo.setBuyersCount(prioritizationInfo.getBuyersCount());
        baseInfo.setRiskBuyersCount(prioritizationInfo.getRiskBuyersCount());

        if (prioritizationInfo.getTendersCount().equals(0L)) {
            baseInfo.setRiskTendersPercent(Double.parseDouble(decimalFormat.format(0)));
            baseInfo.setRiskTendersAmountPercent(Double.parseDouble(decimalFormat.format(0)));
        } else {
            Double riskTendersPercent = (double) prioritizationInfo.getRiskTendersCount() / prioritizationInfo.getTendersCount() * 100;
            baseInfo.setRiskTendersPercent(Double.parseDouble(decimalFormat.format(riskTendersPercent)));

            Double riskTendersAmountPercent = (double) prioritizationInfo.getRiskTendersAmount() / prioritizationInfo.getTendersAmount() * 100;
            baseInfo.setRiskTendersAmountPercent(Double.parseDouble(decimalFormat.format(riskTendersAmountPercent)));
        }

        return baseInfo;
    }

    @Override
    public BuyerTopResponse getTopByRiskTendersCount(DashboardBuyerRequest request) {
        BuyerTopResponse response = new BuyerTopResponse();
        response.setData(mapper.mapDashboardBuyerTopInfo(service.getTopByRiskTendersCount(request)));
        return response;
    }

    @Override
    public BuyerTopResponse getTopByRiskTendersAmount(DashboardBuyerRequest request) {
        BuyerTopResponse response = new BuyerTopResponse();
        response.setData(mapper.mapDashboardBuyerTopInfo(service.getTopByRiskTendersAmount(request)));
        return response;
    }

    @Override
    public BuyerTopByIndicatorCountResponse getTopByIndicatorsCount(DashboardBuyerRequest request) {
        BuyerTopByIndicatorCountResponse response = new BuyerTopByIndicatorCountResponse();
        response.setData(mapper.mapDashboardBuyerTopByIndicatorCountInfo(service.getTopByIndicatorsCount(request)));
        return response;
    }
}
