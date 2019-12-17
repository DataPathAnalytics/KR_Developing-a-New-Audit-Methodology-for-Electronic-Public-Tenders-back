package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.model.dashboard.PrioritizationInfoModel;
import com.datapath.kg.risks.api.dao.service.DashboardTenderDAOService;
import com.datapath.kg.risks.api.dto.dashboard.InfoDTO;
import com.datapath.kg.risks.api.dto.dashboard.tender.RiskTendersValueByMethodDTO;
import com.datapath.kg.risks.api.request.DashboardTenderRequest;
import com.datapath.kg.risks.api.response.dashboard.ByMonthResponse;
import com.datapath.kg.risks.api.response.dashboard.ValueByFieldResponse;
import com.datapath.kg.risks.api.response.dashboard.tender.RiskTendersValueByMethodResponse;
import com.datapath.kg.risks.api.response.dashboard.tender.TenderTopResponse;
import com.datapath.kg.risks.api.response.dashboard.tender.TopOkgzByRiskTendersValueResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class DashboardTenderWebServiceImpl implements DashboardTenderWebService {

    @Autowired
    private DashboardTenderDAOService service;
    @Autowired
    private DTOEntityMapper mapper;

    private static final DecimalFormat decimalFormat = new DecimalFormat("##.0");
    private static final String TENDER_INFO_URL = "http://zakupki.gov.kg/popp/view/order/view.xhtml?id=";

    @Override
    public InfoDTO getInfo(DashboardTenderRequest request) {
        PrioritizationInfoModel prioritizationInfo = service.getPrioritizationInfo(request);

        InfoDTO baseInfo = new InfoDTO();
        baseInfo.setTendersCount(prioritizationInfo.getTendersCount());
        baseInfo.setTendersAmount(prioritizationInfo.getTendersAmount());
        baseInfo.setRiskTendersCount(prioritizationInfo.getRiskTendersCount());
        baseInfo.setRiskTendersAmount(prioritizationInfo.getRiskTendersAmount());

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
    public TenderTopResponse getTopRiskTendersByAmount(DashboardTenderRequest request) {
        TenderTopResponse response = new TenderTopResponse();
        response.setData(mapper.mapDashboardTenderTopInfo(service.getTopRiskTendersByAmount(request)));
        addTenderInfoLinks(response);
        return response;
    }

    @Override
    public TenderTopResponse getTopTendersByIndicatorCount(DashboardTenderRequest request) {
        TenderTopResponse response = new TenderTopResponse();
        response.setData(mapper.mapDashboardTenderTopInfo(service.getTopTendersByIndicatorCount(request)));
        addTenderInfoLinks(response);
        return response;
    }

    @Override
    public ValueByFieldResponse getMethodIndicatorCount(DashboardTenderRequest request) {
        ValueByFieldResponse response = new ValueByFieldResponse();
        response.setData(mapper.mapTenderValueByFields(service.getTendersCountByMethodAndIndicator(request)));
        return response;
    }

    @Override
    public ByMonthResponse getTendersCountInfoByMonth(DashboardTenderRequest request) {
        ByMonthResponse response = new ByMonthResponse();
        response.setData(mapper.mapTenderValueByMonth(service.getTendersCountInfoByMonth(request)));
        return response;
    }

    @Override
    public RiskTendersValueByMethodResponse getRiskTendersAmountInfoByMethod(DashboardTenderRequest request) {
        RiskTendersValueByMethodResponse response = new RiskTendersValueByMethodResponse();
        response.setData(mapper.mapRiskTenderValueByMethod(service.getRiskTendersAmountInfoByMethod(request)));

        Long sum = response.getData()
                .stream()
                .mapToLong(RiskTendersValueByMethodDTO::getValue)
                .sum();

        for (RiskTendersValueByMethodDTO riskMethodAmount : response.getData()) {
            Double percent = (double) riskMethodAmount.getValue() / sum * 100;
            riskMethodAmount.setPercent(Double.parseDouble(decimalFormat.format(percent)));
        }

        return response;
    }

    @Override
    public ValueByFieldResponse getMethodIndicatorAmount(DashboardTenderRequest request) {
        ValueByFieldResponse response = new ValueByFieldResponse();
        response.setData(mapper.mapTenderValueByFields(service.getTendersAmountByMethodAndIndicator(request)));
        return response;
    }

    @Override
    public ByMonthResponse getTendersAmountInfoByMonth(DashboardTenderRequest request) {
        ByMonthResponse response = new ByMonthResponse();
        response.setData(mapper.mapTenderValueByMonth(service.getTendersAmountInfoByMonth(request)));
        return response;
    }

    @Override
    public RiskTendersValueByMethodResponse getRiskTendersCountInfoByMethod(DashboardTenderRequest request) {
        RiskTendersValueByMethodResponse response = new RiskTendersValueByMethodResponse();
        response.setData(mapper.mapRiskTenderValueByMethod(service.getRiskTendersCountInfoByMethod(request)));

        Long sum = response.getData()
                .stream()
                .mapToLong(RiskTendersValueByMethodDTO::getValue)
                .sum();

        for (RiskTendersValueByMethodDTO tenderByMethod : response.getData()) {
            Double percent = (double) tenderByMethod.getValue() / sum * 100;
            tenderByMethod.setPercent(Double.parseDouble(decimalFormat.format(percent)));
        }

        return response;
    }

    @Override
    public TopOkgzByRiskTendersValueResponse getTopOkgzByRiskTendersCount(DashboardTenderRequest request) {
        TopOkgzByRiskTendersValueResponse response = new TopOkgzByRiskTendersValueResponse();
        response.setData(mapper.mapTopOkgzByRiskTendersValue(service.getTopOkgzByRiskTendersCount(request)));
        return response;
    }

    @Override
    public TopOkgzByRiskTendersValueResponse getTopOkgzByRiskTendersAmount(DashboardTenderRequest request) {
        TopOkgzByRiskTendersValueResponse response = new TopOkgzByRiskTendersValueResponse();
        response.setData(mapper.mapTopOkgzByRiskTendersValue(service.getTopOkgzByRiskTendersAmount(request)));
        return response;
    }

    private void addTenderInfoLinks(TenderTopResponse response) {
        response.getData()
                .forEach(t -> t.setLink(TENDER_INFO_URL + t.getTenderId()));
    }
}
