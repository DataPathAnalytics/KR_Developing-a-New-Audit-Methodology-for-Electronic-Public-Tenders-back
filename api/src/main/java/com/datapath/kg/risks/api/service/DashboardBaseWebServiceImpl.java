package com.datapath.kg.risks.api.service;

import com.datapath.kg.risks.api.DTOEntityMapper;
import com.datapath.kg.risks.api.dao.model.dashboard.PrioritizationInfoModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByFieldModel;
import com.datapath.kg.risks.api.dao.service.DashboardBaseDAOService;
import com.datapath.kg.risks.api.dto.dashboard.InfoDTO;
import com.datapath.kg.risks.api.dto.dashboard.ValueByFieldInPercentDTO;
import com.datapath.kg.risks.api.dto.dashboard.base.TopInfoDTO;
import com.datapath.kg.risks.api.request.DashboardBaseRequest;
import com.datapath.kg.risks.api.response.dashboard.ByMonthResponse;
import com.datapath.kg.risks.api.response.dashboard.ValueByFieldInPercentResponse;
import com.datapath.kg.risks.api.response.dashboard.ValueByFieldResponse;
import com.datapath.kg.risks.api.response.dashboard.base.BaseTopResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardBaseWebServiceImpl implements DashboardBaseWebService {

    @Autowired
    private DashboardBaseDAOService service;
    @Autowired
    private DTOEntityMapper mapper;

    private DecimalFormat decimalFormat = new DecimalFormat("##.0");
    private DecimalFormat decimalFormatIndicatorPercent = new DecimalFormat("##.00");

    @Override
    public InfoDTO getInfo(DashboardBaseRequest request) {
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
    public BaseTopResponse getTopRegionsByRiskTendersCount(DashboardBaseRequest request) {
        BaseTopResponse response = new BaseTopResponse();
        response.setData(mapper.mapDashboardBaseTopInfo(service.getTopRegionsByRiskTendersCount(request)));
        return response;
    }

    @Override
    public BaseTopResponse getTopMethodsByRiskTendersCount(DashboardBaseRequest request) {
        BaseTopResponse response = new BaseTopResponse();
        response.setData(mapper.mapDashboardBaseTopInfo(service.getTopMethodsByRiskTendersCount(request)));
        return response;
    }

    @Override
    public BaseTopResponse getTopOkgzByRiskTendersCount(DashboardBaseRequest request) {
        BaseTopResponse response = new BaseTopResponse();
        response.setData(mapper.mapDashboardBaseTopInfo(service.getTopOkgzByRiskTendersCount(request)));
        return response;
    }

    @Override
    public BaseTopResponse getTopRegionsByAmount(DashboardBaseRequest request) {
        BaseTopResponse response = new BaseTopResponse();
        response.setData(mapper.mapDashboardBaseTopInfo(service.getTopRegionsByAmount(request)));
        return response;
    }

    @Override
    public BaseTopResponse getTopMethodsByAmount(DashboardBaseRequest request) {
        BaseTopResponse response = new BaseTopResponse();
        response.setData(mapper.mapDashboardBaseTopInfo(service.getTopMethodsByAmount(request)));
        return response;
    }

    @Override
    public BaseTopResponse getTopOkgzByAmount(DashboardBaseRequest request) {
        BaseTopResponse response = new BaseTopResponse();
        response.setData(mapper.mapDashboardBaseTopInfo(service.getTopOkgzByAmount(request)));
        return response;
    }

    @Override
    public ValueByFieldResponse getRegionIndicatorCount(DashboardBaseRequest request) {
        ValueByFieldResponse response = new ValueByFieldResponse();
        response.setData(mapper.mapTenderValueByFields(service.getTendersCountByRegionAndIndicator(request)));
        return response;
    }

    @Override
    public ValueByFieldResponse getRegionIndicatorAmount(DashboardBaseRequest request) {
        ValueByFieldResponse response = new ValueByFieldResponse();
        response.setData(mapper.mapTenderValueByFields(service.getTendersAmountByRegionAndIndicator(request)));
        return response;
    }

    @Override
    public ByMonthResponse getCountByMonth(DashboardBaseRequest request) {
        ByMonthResponse response = new ByMonthResponse();
        response.setData(mapper.mapTenderValueByMonth(service.getTendersCountByMonth(request)));
        return response;
    }

    @Override
    public ByMonthResponse getAmountByMonth(DashboardBaseRequest request) {
        ByMonthResponse response = new ByMonthResponse();
        response.setData(mapper.mapTenderValueByMonth(service.getTendersAmountByMonth(request)));
        return response;
    }

    @Override
    public TopInfoDTO getTopInfo(DashboardBaseRequest request) {
        TopInfoDTO response = new TopInfoDTO();

        try {
            response.setTender(service.getTopTender(request));
            response.setBuyer(service.getTopBuyer(request));
            response.setIndicator(service.getTopIndicator(request));
        } catch (EmptyResultDataAccessException e) {
            response.setTender(null);
            response.setBuyer(null);
            response.setIndicator(null);
        }
        return response;
    }

    @Override
    public ValueByFieldInPercentResponse getRegionIndicatorCountPercent(DashboardBaseRequest request) {
        ValueByFieldInPercentResponse response = new ValueByFieldInPercentResponse();
        List<ValueByFieldModel> tendersCountByRegionAndIndicator = service.getTendersCountByRegionAndIndicator(request);

        response.setData(convertLongDataToPercentByRegion(tendersCountByRegionAndIndicator));

        return response;
    }

    @Override
    public ValueByFieldInPercentResponse getRegionIndicatorAmountPercent(DashboardBaseRequest request) {
        ValueByFieldInPercentResponse response = new ValueByFieldInPercentResponse();
        List<ValueByFieldModel> tendersAmountByRegionAndIndicator = service.getTendersAmountByRegionAndIndicator(request);

        response.setData(convertLongDataToPercentByRegion(tendersAmountByRegionAndIndicator));

        return response;
    }

    private List<ValueByFieldInPercentDTO> convertLongDataToPercentByRegion(List<ValueByFieldModel> data) {
        List<ValueByFieldInPercentDTO> result = new ArrayList<>();

        Map<String, List<ValueByFieldModel>> dataByRegion = data.stream()
                .collect(Collectors.groupingBy(ValueByFieldModel::getDescription));

        for (Map.Entry<String, List<ValueByFieldModel>> regionData : dataByRegion.entrySet()) {
            long sumByRegion = regionData.getValue().stream()
                    .mapToLong(ValueByFieldModel::getValue)
                    .sum();

            regionData.getValue().stream()
                    .map(t -> {
                        if (!t.getValue().equals(0L)) {
                            Double percentValue = (double) t.getValue() / sumByRegion * 100;
                            return new ValueByFieldInPercentDTO(
                                    t.getDescription(),
                                    t.getIndicatorId(),
                                    Double.parseDouble(decimalFormatIndicatorPercent.format(percentValue)));
                        } else {
                            return new ValueByFieldInPercentDTO(
                                    t.getDescription(),
                                    t.getIndicatorId(),
                                    Double.parseDouble(decimalFormatIndicatorPercent.format(0)));
                        }
                    }).forEach(result::add);
        }

        return result;
    }
}
