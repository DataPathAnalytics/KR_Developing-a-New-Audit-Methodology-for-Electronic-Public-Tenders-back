package com.datapath.kg.risks.api;

import com.datapath.kg.risks.api.dao.entity.*;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByFieldModel;
import com.datapath.kg.risks.api.dao.model.dashboard.ValueByMonthModel;
import com.datapath.kg.risks.api.dao.model.dashboard.base.BaseTopModel;
import com.datapath.kg.risks.api.dao.model.dashboard.buyer.BuyerTopModel;
import com.datapath.kg.risks.api.dao.model.dashboard.tender.RiskTendersValueByMethodModel;
import com.datapath.kg.risks.api.dao.model.dashboard.tender.TenderTopModel;
import com.datapath.kg.risks.api.dao.model.dashboard.tender.TopOkgzByRiskTendersValueModel;
import com.datapath.kg.risks.api.dto.*;
import com.datapath.kg.risks.api.dto.dashboard.ValueByFieldDTO;
import com.datapath.kg.risks.api.dto.dashboard.ValueByMonthDTO;
import com.datapath.kg.risks.api.dto.dashboard.base.BaseTopDTO;
import com.datapath.kg.risks.api.dto.dashboard.buyer.BuyerTopByIndicatorCountDTO;
import com.datapath.kg.risks.api.dto.dashboard.buyer.BuyerTopDTO;
import com.datapath.kg.risks.api.dto.dashboard.tender.RiskTendersValueByMethodDTO;
import com.datapath.kg.risks.api.dto.dashboard.tender.TenderTopDTO;
import com.datapath.kg.risks.api.dto.dashboard.tender.TopOkgzByRiskTendersValueDTO;
import com.datapath.kg.risks.api.request.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DTOEntityMapper {

    TemplateDTO map(TemplateEntity entity);

    TemplateEntity map(TemplateDTO dto);

    TemplateEntity map(AddBaseTemplateRequest request);

    TemplateEntity map(AddAuditorTemplateRequest request);

    QuestionCategoryDTO map(QuestionCategoryEntity entity);

    QuestionCategoryEntity map(AddQuestionCategoryRequest request);

    QuestionDTO map(QuestionEntity entity);

    QuestionEntity map(AddQuestionRequest request);

    @Mapping(ignore = true, target = "permissions")
    AuditorDTO map(AuditorEntity entity);

    @Mapping(ignore = true, target = "permissions")
    AuditorEntity map(AuditorDTO dto);

    PermissionDTO map(PermissionEntity entity);

    TemplateTypeDTO map(TemplateTypeEntity entity);

    TenderPrioritizationDTO map(TenderPrioritizationEntity entity);

    BuyerPrioritizationDTO map(BuyerPrioritizationEntity entity);

    BuyerDTO map(BuyerEntity entity);

    List<BuyerDTO> mapBuyers(List<BuyerEntity> entities);

    ChecklistEntity map(SaveChecklistRequest request);

    ChecklistDTO map(ChecklistEntity entity);

    List<AnswerTypeDTO> mapAnswerTypes(List<AnswerTypeEntity> entities);

    List<ComponentImpactDTO> mapComponentImpacts(List<ComponentImpactEntity> entities);

    List<RiskLevelDTO> mapRiskLevels(List<RiskLevelEntity> entities);

    List<IndicatorDTO> mapIndicators(List<IndicatorEntity> entities);

    List<OkgzDTO> mapOkgz(List<OkgzEntity> entities);

    List<ChecklistScoreDTO> mapChecklists(List<ChecklistScoreEntity> entities);

    AuditorSettingsDTO mapAuditorSettings(AuditorEntity entity);

    List<ChecklistStatusDTO> mapChecklistStatuses(List<ChecklistStatusEntity> entity);

    ChecklistStatusDTO mapChecklistStatus(ChecklistStatusEntity entity);

    ChecklistScoreDTO map(ChecklistScoreEntity entity);

    List<AuditNameDTO> mapChecklistAuditName(List<ChecklistEntity> entities);

    List<AuditorNameDTO> mapChecklistAuditorName(List<AuditorEntity> entities);

    List<TemplateTypeDTO> mapTemplateTypes(List<TemplateTypeEntity> entities);

    List<BaseTopDTO> mapDashboardBaseTopInfo(List<BaseTopModel> models);

    List<ValueByFieldDTO> mapTenderValueByFields(List<ValueByFieldModel> models);

    List<ValueByMonthDTO> mapTenderValueByMonth(List<ValueByMonthModel> models);

    List<TenderTopDTO> mapDashboardTenderTopInfo(List<TenderTopModel> models);

    List<RiskTendersValueByMethodDTO> mapRiskTenderValueByMethod(List<RiskTendersValueByMethodModel> models);

    List<TopOkgzByRiskTendersValueDTO> mapTopOkgzByRiskTendersValue(List<TopOkgzByRiskTendersValueModel> models);

    List<BuyerTopDTO> mapDashboardBuyerTopInfo(List<BuyerTopModel> models);

    List<BuyerTopByIndicatorCountDTO> mapDashboardBuyerTopByIndicatorCountInfo(List<BuyerTopModel> models);

    QuestionDTO map(ChecklistQuestionEntity entity);

    List<QuestionCategoryDTO> mapCategories(List<QuestionCategoryEntity> entities);

}