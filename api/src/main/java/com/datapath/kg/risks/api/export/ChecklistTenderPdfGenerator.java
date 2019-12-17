package com.datapath.kg.risks.api.export;

import com.datapath.kg.risks.api.dao.entity.BidEntity;
import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import com.datapath.kg.risks.api.dao.entity.ChecklistIndicatorEntity;
import com.datapath.kg.risks.api.dao.entity.TenderPrioritizationEntity;
import com.datapath.kg.risks.api.export.utils.CellBuilder;
import com.datapath.kg.risks.api.export.utils.CellSettings;
import com.datapath.kg.risks.api.export.utils.PdfFont;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class ChecklistTenderPdfGenerator extends ChecklistPdfGeneratorAbs {

    @Autowired
    private PdfFont font;

    void generateTenderReport(Document document, ChecklistEntity checklist) throws DocumentException {
        addBaseTenderInfo(document, checklist);
        addAnswerQuestionInfo(document, checklist);
        addIndicatorsInfo(document, checklist);
        addScoreInfo(document, checklist, "ОЦЕНКА РЕЗУЛЬТАТОВ АУДИТА ЗАКУПКИ");
        addSummaryInfo(document, checklist);
        addSignature(document, "Представитель СП КР:");
        addSignature(document, "Представитель аудируемого объекта:");
    }

    private void addIndicatorsInfo(Document document, ChecklistEntity checklist) throws DocumentException {
        if (!isEmpty(checklist.getIndicators())) {
            document.setPageSize(PageSize.A4.rotate());
            document.newPage();

            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setSpacingAfter(10);

            titleTable.addCell(CellBuilder.build(new Phrase("РЕЗУЛЬТАТЫ АВТОМАТИЧЕСКИХ ИНДИКАТОРОВ РИСКА КОНКУРСА:", font.getCalibri11BlueBoldFont()),
                    CellSettings.builder()
                            .border(0)
                            .backgroundColor(new BaseColor(222, 234, 246))
                            .build())
            );
            document.add(titleTable);

            PdfPTable infoTable = new PdfPTable(new float[]{1, 3, 3, 7, 2, 3, 4});
            infoTable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            addIndicatorInfoHeaders(infoTable);

            for (int i = 0; i < checklist.getIndicators().size(); i++) {
                ChecklistIndicatorEntity indicator = checklist.getIndicators().get(i);

                infoTable.addCell(new Phrase(String.valueOf(i + 1), font.getCalibri9NormalFont()));
                infoTable.addCell(new Phrase(indicator.getIndicator().getName() + ":\n" + indicator.getIndicator().getDescription(), font.getCalibri9NormalFont()));
                infoTable.addCell(new Phrase(indicator.getIndicator().getRisks(), font.getCalibri9NormalFont()));
                infoTable.addCell(new Phrase(indicator.getIndicator().getLawViolation(), font.getCalibri9NormalFont()));

                String riskLevel = indicator.getIndicator().getRiskLevelText();
                PdfPCell riskLevelCell = new PdfPCell(new Phrase(riskLevel, font.getCalibri9NormalFont()));
                riskLevelCell.setBorderColor(BaseColor.LIGHT_GRAY);
                switch (riskLevel) {
                    case "Очень низкий":
                        riskLevelCell.setBackgroundColor(new BaseColor(226,239,218));
                        break;
                    case "Низкий":
                        riskLevelCell.setBackgroundColor(new BaseColor(169,208,142));
                        break;
                    case "Средний":
                        riskLevelCell.setBackgroundColor(new BaseColor(255,192,0));
                        break;
                    case "Высокий":
                        riskLevelCell.setBackgroundColor(new BaseColor(255,153,153));
                        break;
                    default:
                        riskLevelCell.setBackgroundColor(new BaseColor(255,102,0));
                }
                infoTable.addCell(riskLevelCell);

                if (isNull(indicator.getAnswerType())) {
                    infoTable.addCell(new Phrase(""));
                } else {
                    infoTable.addCell(new Phrase(indicator.getAnswerType().getName(), font.getCalibri9NormalFont()));
                }
                infoTable.addCell(new Phrase(indicator.getComment(), font.getCalibri9NormalFont()));
            }

            document.add(infoTable);

            document.setPageSize(PageSize.A4);
            document.newPage();
        }
    }

    private void addIndicatorInfoHeaders(PdfPTable table) {
        CellSettings settings = CellSettings.builder()
                .backgroundColor(new BaseColor(47, 84, 150))
                .borderColor(BaseColor.LIGHT_GRAY)
                .build();

        table.addCell(CellBuilder.build(new Phrase("No.", font.getCalibri9WhiteBoldFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("Индикатор", font.getCalibri9WhiteBoldFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("Риск", font.getCalibri9WhiteBoldFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("Нарушение НПА", font.getCalibri9WhiteBoldFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("Уровень риска", font.getCalibri9WhiteBoldFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("Риск подтвердился аудитом", font.getCalibri9WhiteBoldFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("Примечания", font.getCalibri9WhiteBoldFont()), settings));
        table.setHeaderRows(1);
    }

    private void addBaseTenderInfo(Document document, ChecklistEntity checklist) throws DocumentException {
        PdfPTable baseTable = new PdfPTable(2);
        baseTable.setSpacingAfter(20);

        PdfPTable leftTable = new PdfPTable(new float[]{2, 3});
        leftTable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

//        addAuditName(leftTable, checklist);
//        addPeriod(leftTable, checklist);
        addBuyer(leftTable, checklist);
        addDate(leftTable, checklist);
        addContractNo(leftTable, checklist);
        addContractAmount(leftTable, checklist);
        addContractDescription(leftTable, checklist);
        addSupplier(leftTable, checklist);

        PdfPCell leftCell = new PdfPCell(leftTable);
        leftCell.setPaddingRight(5);
        leftCell.setBorder(0);
        baseTable.addCell(leftCell);

        PdfPTable rightTable = new PdfPTable(new float[]{3, 2});
        rightTable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

        addTitle(rightTable);
        addTenderNumber(rightTable, checklist);
        addPublishedDate(rightTable, checklist);
        addPeriodStart(rightTable, checklist);
        addPeriodEnd(rightTable, checklist);
        addDateDisclosed(rightTable, checklist);
        addGuarantee(rightTable, checklist);
        addContractDate(rightTable, checklist);

        PdfPCell rightCell = new PdfPCell(rightTable);
        rightCell.setBorder(0);
        rightCell.setPaddingLeft(5);
        baseTable.addCell(rightCell);

        document.add(baseTable);
    }

    private void addContractDate(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Дата договора:", font.getCalibri10BlueNormalFont()));

        TenderPrioritizationEntity tenderPrioritization = prioritizationDAOService
                .getTenderPrioritizationById(checklist.getTender().getId());

        if (!isNull(tenderPrioritization)) {
            table.addCell(new Phrase(tenderPrioritization.getContractSigningDate(), font.getCalibri10NormalFont()));
        } else {
            table.addCell(new Phrase());
        }
    }

    private void addGuarantee(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Размер гарантии КЗ:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(nonNull(checklist.getTender().getGuaranteeAmount()) ?
                checklist.getTender().getGuaranteeAmount().toString() :
                null, font.getCalibri10NormalFont()));
    }

    private void addDateDisclosed(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Дата вскрытия КЗ:", font.getCalibri10BlueNormalFont()));

        if (!isEmpty(checklist.getTender().getBids())) {
            Optional<BidEntity> bid = checklist.getTender()
                    .getBids()
                    .stream()
                    .filter(b -> nonNull(b.getDateDisclosed()))
                    .findFirst();

            if (bid.isPresent()) {
                table.addCell(new Phrase(bid.get().getDateDisclosed().toString(), font.getCalibri10NormalFont()));
            } else {
                table.addCell(new Phrase());
            }
        } else {
            table.addCell(new Phrase());
        }
    }

    private void addPeriodEnd(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Окончательный срок подачи КЗ:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(nonNull(checklist.getTender().getPeriodEndDate()) ?
                checklist.getTender().getPeriodEndDate().toString() :
                null, font.getCalibri10NormalFont()));
    }

    private void addPeriodStart(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Дата подачи конкурсных заявок (КЗ):", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(nonNull(checklist.getTender().getPeriodStartDate()) ?
                checklist.getTender().getPeriodStartDate().toString() :
                null, font.getCalibri10NormalFont()));
    }

    private void addPublishedDate(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Дата объявления:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(nonNull(checklist.getTender().getDatePublished()) ?
                checklist.getTender().getDatePublished().toString() :
                null, font.getCalibri10NormalFont()));
    }

    private void addTenderNumber(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Идентификатор конкурса:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(checklist.getTender().getNumber(), font.getCalibri10NormalFont()));
    }

    private void addAuditName(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Аудит закупок:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(checklist.getAuditName(), font.getCalibri10NormalFont()));
    }

    private void addBuyer(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Проверяемая организация:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(checklist.getBuyer().getIdentifierLegalNameRu(), font.getCalibri10NormalFont()));
    }

    private void addDate(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Дата заполнения:", font.getCalibri10BlueNormalFont()));
        table.addCell(nonNull(checklist.getModifiedDate()) ?
                new Phrase(checklist.getModifiedDate().toString(), font.getCalibri10NormalFont()) :
                null);
    }

    private void addContractNo(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Договор No:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(checklist.getContractNumber(), font.getCalibri10NormalFont()));
    }

    private void addContractAmount(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Сумма Договора:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(nonNull(checklist.getContractAmount()) ?
                checklist.getContractAmount().toString() :
                null, font.getCalibri10NormalFont()));
    }

    private void addContractDescription(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Описание договора:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(checklist.getContractDescription(), font.getCalibri10NormalFont()));

    }

    private void addSupplier(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Подрядчик:", font.getCalibri10BlueNormalFont()));
        table.addCell(new Phrase(checklist.getSupplier(), font.getCalibri10NormalFont()));
    }

    private void addTitle(PdfPTable table) {
        PdfPCell titleCell = new PdfPCell(new Phrase("Данные о конкурсе из ЭСГ", font.getCalibri10GreenBoldFont()));
        titleCell.setColspan(2);
        titleCell.setBorderColor(BaseColor.LIGHT_GRAY);
        table.addCell(titleCell);
    }
}
