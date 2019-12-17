package com.datapath.kg.risks.api.export;

import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import com.datapath.kg.risks.api.export.utils.CellBuilder;
import com.datapath.kg.risks.api.export.utils.CellSettings;
import com.datapath.kg.risks.api.export.utils.PdfFont;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Service
public class ChecklistBuyerPdfGenerator extends ChecklistPdfGeneratorAbs {

    @Autowired
    private PdfFont font;

    void generateBuyerReport(Document document, ChecklistEntity checklist) throws IOException, DocumentException {
        addBaseBuyerInfo(document, checklist);
        addAnswerQuestionInfo(document, checklist);
//        addTendersInfo(document, checklist);
        document.newPage();
        addTendersScoreInfo(document, checklist);
        addTendersComment(document, checklist);
        addScoreInfo(document, checklist, "ОЦЕНКА РЕЗУЛЬТАТОВ АУДИТА ЗАКУПАЮЩЕЙ ОРГАНИЗАЦИИ");
        addSummaryInfo(document, checklist);
        addSignature(document, "Представитель СП КР:");
        addSignature(document, "Представитель аудируемого объекта:");
    }

    private void addTendersComment(Document document, ChecklistEntity checklist) throws DocumentException {
        if (!StringUtils.isEmpty(checklist.getTendersComment())) {
            PdfPTable table = new PdfPTable(1);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            table.addCell(CellBuilder.build(new Phrase("Краткое изложение проблем и спорных вопросов закупочной практики организации", font.getCalibri11BlueBoldFont()),
                    CellSettings.builder()
                            .horizontalAlignment(Element.ALIGN_CENTER)
                            .backgroundColor(new BaseColor(222, 234, 246))
                            .borderColor(BaseColor.LIGHT_GRAY)
                            .build())
            );
            table.addCell(CellBuilder.build(new Phrase(checklist.getTendersComment(), font.getCalibri11NormalFont()),
                    CellSettings.builder()
                            .backgroundColor(new BaseColor(222, 234, 246))
                            .borderColor(BaseColor.LIGHT_GRAY)
                            .build()));
            document.add(table);
        }
    }

    private void addTendersScoreInfo(Document document, ChecklistEntity checklist) throws DocumentException {
        if (nonNull(checklist.getAutoTendersScore())) {

            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setSpacingAfter(15);
            titleTable.addCell(CellBuilder.build(new Phrase("ОЦЕНКА РЕЗУЛЬТАТОВ ЗАКУПОЧНОЙ ПРАКТИКИ", font.getCalibri11BlueBoldFont()),
                    CellSettings.builder()
                            .backgroundColor(new BaseColor(222, 234, 246))
                            .border(0)
                            .build())
            );
            document.add(titleTable);

            CellSettings alignCenterSetting = CellSettings.builder()
                    .horizontalAlignment(Element.ALIGN_CENTER)
                    .verticalAlignment(Element.ALIGN_CENTER)
                    .build();

            if (nonNull(checklist.getAutoTendersScore())) {
                addScore(document,
                        checklist.getAutoTendersScore(),
                        "Результат автоматического скоринга оценки\n" +
                                "закупочной практики на основе проверенных\n" +
                                "конкурсов организации",
                        alignCenterSetting);
            }

            if (nonNull(checklist.getManualTendersScore())) {
                addScore(document,
                        checklist.getManualTendersScore(),
                        "Оценка уровня риска закупочной практики\n" +
                                "организации представителем СП КР",
                        alignCenterSetting);
            }
        }
    }

    private void addBaseBuyerInfo(Document document, ChecklistEntity checklist) throws DocumentException, IOException {
        PdfPTable baseTable = new PdfPTable(new float[]{5, 1});
        baseTable.setSpacingAfter(10);

        PdfPTable infoTable = new PdfPTable(new float[]{2, 5});
//        infoTable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
        infoTable.getDefaultCell().setBorder(0);
        infoTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_CENTER);

        addEmptyRow(infoTable);
//        addAuditName(infoTable, checklist);
        addPeriod(infoTable, checklist);
        addBuyer(infoTable, checklist);
        addDate(infoTable, checklist);
        addEmptyRow(infoTable);

        PdfPCell infoCell = new PdfPCell(infoTable);
        infoCell.setBorder(0);
        baseTable.addCell(infoCell);

        PdfPCell logoCell = new PdfPCell(Image.getInstance(StreamUtils.copyToByteArray(new ClassPathResource("images/logo.png").getInputStream())), true);
        logoCell.setBorder(0);
        logoCell.setFixedHeight(55);
        logoCell.setPaddingRight(10);
        logoCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        baseTable.addCell(logoCell);

        document.add(baseTable);
    }

    private void addEmptyRow(PdfPTable table) {
        PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
        emptyCell.setBorder(0);

        table.addCell(emptyCell);
        table.addCell(emptyCell);
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
        table.addCell(new Phrase(nonNull(checklist.getModifiedDate()) ?
                checklist.getModifiedDate().toString() :
                null, font.getCalibri10NormalFont()));
    }

    private void addTendersInfo(Document document, ChecklistEntity checklist) throws DocumentException {
        if (!StringUtils.isEmpty(checklist.getTendersComment())) {
            CellSettings settings = CellSettings.builder()
                    .backgroundColor(new BaseColor(222, 234, 246))
                    .border(0)
                    .build();

            if (writer.getVerticalPosition(true) < 300) {
                document.newPage();
            }

            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setSpacingBefore(15);
            titleTable.setSpacingAfter(15);
            titleTable.addCell(CellBuilder.build(
                    new Phrase("ВЫВОД ПРЕДСТАВИТЕЛЯ СП КР ПО РЕЗУЛЬТАТАМ АУДИТА КОНКУРСОВ ЗАКУПАЮЩЕЙ ОРГАНИЗАЦИИ:", font.getCalibri11BlueBoldFont()), settings)
            );
            document.add(titleTable);

            PdfPTable dataTable = new PdfPTable(1);
            dataTable.setSpacingAfter(10);
            dataTable.addCell(CellBuilder.build(new Phrase(checklist.getTendersComment(), font.getCalibri11NormalFont()), settings));
            document.add(dataTable);
        }
    }
}
