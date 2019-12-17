package com.datapath.kg.risks.api.export;

import com.datapath.kg.risks.api.comparators.AnswerEntityComparator;
import com.datapath.kg.risks.api.dao.entity.AnswerEntity;
import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import com.datapath.kg.risks.api.dao.entity.ChecklistScoreEntity;
import com.datapath.kg.risks.api.dao.service.PrioritizationDAOService;
import com.datapath.kg.risks.api.export.utils.CellBuilder;
import com.datapath.kg.risks.api.export.utils.CellSettings;
import com.datapath.kg.risks.api.export.utils.PdfFont;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public abstract class ChecklistPdfGeneratorAbs {

    @Setter
    protected PdfWriter writer;

    @Autowired
    private PdfFont font;
    @Autowired
    private AnswerEntityComparator answerComparator;
    @Autowired
    protected PrioritizationDAOService prioritizationDAOService;

    void addSummaryInfo(Document document, ChecklistEntity checklist) throws DocumentException {
        if (!StringUtils.isEmpty(checklist.getSummary())) {
            PdfPTable table = new PdfPTable(1);
            table.setSpacingBefore(10);
            table.setSpacingAfter(20);

            table.addCell(CellBuilder.build(new Phrase("Краткое изложение проблем и спорных вопросов - Общее резюме обзора", font.getCalibri11BlueBoldFont()),
                    CellSettings.builder()
                            .horizontalAlignment(Element.ALIGN_CENTER)
                            .backgroundColor(new BaseColor(222, 234, 246))
                            .borderColor(BaseColor.LIGHT_GRAY)
                            .build())
            );
            table.addCell(CellBuilder.build(new Phrase(checklist.getSummary(), font.getCalibri11NormalFont()),
                    CellSettings.builder()
                            .backgroundColor(new BaseColor(222, 234, 246))
                            .borderColor(BaseColor.LIGHT_GRAY)
                            .build()));
            document.add(table);
        }
    }

    void addSignature(Document document, String title) throws DocumentException {
        PdfPTable table = new PdfPTable(new float[]{3, 2, 2, 2});
        table.setSpacingBefore(10);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        CellSettings settings = CellSettings.builder()
                .border(0)
                .build();

        CellSettings settingsWithAlign = CellSettings.builder()
                .border(0)
                .horizontalAlignment(Element.ALIGN_CENTER)
                .build();


        table.addCell(CellBuilder.build(new Phrase(title, font.getCalibri11NormalFont()), settings));

        PdfPCell lineCell = CellBuilder.build(new Phrase("_______________", font.getCalibri11NormalFont()), settingsWithAlign);
        table.addCell(lineCell);
        table.addCell(lineCell);
        table.addCell(lineCell);

        table.addCell(CellBuilder.build(new Phrase("", font.getCalibri11NormalFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("(Подпись)", font.getCalibri11NormalFont()), settingsWithAlign));
        table.addCell(CellBuilder.build(new Phrase("(ФИО)", font.getCalibri11NormalFont()), settingsWithAlign));
        table.addCell(CellBuilder.build(new Phrase("(Дата)", font.getCalibri11NormalFont()), settingsWithAlign));

        document.add(table);
    }

    void addScoreInfo(Document document, ChecklistEntity checklist, String title) throws DocumentException {
        if (nonNull(checklist.getAutoScore()) || nonNull(checklist.getManualScore())) {

            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setSpacingAfter(15);
            titleTable.addCell(CellBuilder.build(new Phrase(title, font.getCalibri11BlueBoldFont()),
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

            if (nonNull(checklist.getAutoScore())) {
                addScore(document,
                        checklist.getAutoScore(),
                        "Результат оценки аудита с применением автоматической (скоринговой) методологии",
                        alignCenterSetting);
            }

            if (nonNull(checklist.getManualScore())) {
                addScore(document,
                        checklist.getManualScore(),
                        "Итоговая оценка аудита представителя СП КР",
                        alignCenterSetting);
            }
        }
    }

    void addScore(Document document, ChecklistScoreEntity score, String title, CellSettings settings) throws DocumentException {
        PdfPTable baseTable = new PdfPTable(4);
        baseTable.getDefaultCell().setBorder(0);
        baseTable.setSpacingAfter(15);

        PdfPTable nameTable = new PdfPTable(1);

        nameTable.addCell(CellBuilder.build(new Phrase(title, font.getCalibri11BlackBoldFont()),
                settings));

        PdfPCell nameCell = new PdfPCell(nameTable);
        nameCell.setColspan(2);
        nameCell.setPaddingRight(10);
        nameCell.setBorder(0);
        baseTable.addCell(nameCell);

        PdfPCell dataCell = CellBuilder.build(new Phrase(score.getName(), font.getCalibri11BlackBoldFont()), settings);
        dataCell.setBackgroundColor(getScoreBackgroundColor(score.getId()));
        baseTable.addCell(dataCell);

        baseTable.addCell(new Phrase(""));

        document.add(baseTable);
    }

    private BaseColor getScoreBackgroundColor(Integer id) {
        switch (id) {
            case 1:
                return new BaseColor(226, 239, 218);
            case 2:
                return new BaseColor(169, 208, 142);
            case 3:
                return new BaseColor(255, 192, 0);
            case 4:
                return new BaseColor(255, 153, 153);
            case 5:
                return new BaseColor(255, 102, 0);
            default:
                return new BaseColor(192, 0, 0);
        }
    }

    void addAnswerQuestionInfo(Document document, ChecklistEntity checklist) throws DocumentException {
        if (!isEmpty(checklist.getAnswers())) {

            PdfPTable title = new PdfPTable(1);
            title.addCell(CellBuilder.build(new Phrase("ВОПРОСЫ КОНТРОЛЬНОГО ЛИСТА", font.getCalibri11BlueBoldFont()),
                    CellSettings.builder()
                            .backgroundColor(new BaseColor(222, 234, 246))
                            .border(0)
                            .verticalAlignment(Element.ALIGN_JUSTIFIED)
                            .build())
            );
            document.add(title);

            checklist.getAnswers().sort(answerComparator);

            Map<String, java.util.List<AnswerEntity>> answers = checklist.getAnswers()
                    .stream()
                    .collect(Collectors.groupingBy(
                            AnswerEntity::getCategoryName,
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));

            addAnswerQuestionTableData(document, answers);
        }
    }

    private void addAnswerQuestionTableData(Document document, Map<String, java.util.List<AnswerEntity>> answers) throws DocumentException {
        CellSettings alignCenterSettings = CellSettings.builder()
                .horizontalAlignment(Element.ALIGN_CENTER)
                .verticalAlignment(Element.ALIGN_MIDDLE)
                .borderColor(BaseColor.LIGHT_GRAY)
                .build();

        CellSettings alignCenterBottomSettings = CellSettings.builder()
                .horizontalAlignment(Element.ALIGN_CENTER)
                .verticalAlignment(Element.ALIGN_BOTTOM)
                .borderColor(BaseColor.LIGHT_GRAY)
                .build();

        for (Map.Entry<String, java.util.List<AnswerEntity>> answer : answers.entrySet()) {

            if (writer.getVerticalPosition(true) < 200) {
                document.newPage();
            }

            addAnswerQuestionTableCategory(document, answer.getKey());

            PdfPTable answerQuestionTable = new PdfPTable(new float[]{1, 8, 2, 8, 4});
            answerQuestionTable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            addAnswerQuestionTableHeaders(answerQuestionTable);

            List<AnswerEntity> value = answer.getValue();
            for (AnswerEntity aValue : value) {
                answerQuestionTable.addCell(CellBuilder.build(new Phrase(aValue.getQuestionNumber(), font.getCalibri9NormalFont()), alignCenterBottomSettings));

                answerQuestionTable.addCell(new Phrase(aValue.getQuestionDescription(), font.getCalibri9NormalFont()));

                if (nonNull(aValue.getAnswerType())) {
                    PdfPCell answerCell = CellBuilder.build(new Phrase(aValue.getAnswerType().getName(), font.getCalibri9NormalFont()), alignCenterSettings);
                    switch (aValue.getAnswerType().getId()) {
                        case 1:
                            answerCell.setBackgroundColor(new BaseColor(226, 239, 217));
                            break;
                        case 2:
                            answerCell.setBackgroundColor(new BaseColor(246, 126, 102));
                            break;
                        default:
                            answerCell.setBackgroundColor(new BaseColor(237, 237, 237));
                    }
                    answerQuestionTable.addCell(answerCell);
                } else {
                    answerQuestionTable.addCell(new Phrase());
                }
                answerQuestionTable.addCell(new Phrase(aValue.getComment(), font.getCalibri9NormalFont()));
                answerQuestionTable.addCell(new Phrase(aValue.getNpa(), font.getCalibri9NormalFont()));
            }
            document.add(answerQuestionTable);
        }
    }

    private void addAnswerQuestionTableCategory(Document document, String category) throws DocumentException {
        PdfPTable categoryName = new PdfPTable(new float[]{1, 8, 2, 8, 4});
        categoryName.setSpacingBefore(20);
        categoryName.setSpacingAfter(5);
        categoryName.addCell(
                CellBuilder.build(new Phrase(category, font.getCalibri11BlueBoldFont()),
                        CellSettings.builder()
                                .colspan(5)
                                .border(0)
                                .build())
        );
        document.add(categoryName);
    }

    private void addAnswerQuestionTableHeaders(PdfPTable table) {
        CellSettings settings = CellSettings.builder()
                .backgroundColor(new BaseColor(47, 84, 150))
                .horizontalAlignment(Element.ALIGN_CENTER)
                .borderColor(BaseColor.LIGHT_GRAY)
                .build();

        CellSettings leftAlignSetting = CellSettings.builder()
                .backgroundColor(new BaseColor(47, 84, 150))
                .horizontalAlignment(Element.ALIGN_LEFT)
                .borderColor(BaseColor.LIGHT_GRAY)
                .build();

        table.addCell(CellBuilder.build(new Phrase("No.", font.getCalibri12WhiteBoldFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("Вопросы", font.getCalibri12WhiteBoldFont()), leftAlignSetting));
        table.addCell(CellBuilder.build(new Phrase("Ответ", font.getCalibri12WhiteBoldFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("Примечания", font.getCalibri12WhiteBoldFont()), settings));
        table.addCell(CellBuilder.build(new Phrase("Ссылка на НПА", font.getCalibri12WhiteBoldFont()), settings));
        table.setHeaderRows(1);
    }

    void addPeriod(PdfPTable table, ChecklistEntity checklist) {
        table.addCell(new Phrase("Период проведения аудита:", font.getCalibri10BlueNormalFont()));

        StringBuilder period = new StringBuilder();

        if (nonNull(checklist.getStartDate())) {
            period
                    .append("c ")
                    .append(checklist.getStartDate())
                    .append(" ");
        }

        if (nonNull(checklist.getEndDate())) {
            period
                    .append("по ")
                    .append(checklist.getEndDate());
        }
        table.addCell(new Phrase(period.toString(), font.getCalibri10NormalFont()));
    }
}
