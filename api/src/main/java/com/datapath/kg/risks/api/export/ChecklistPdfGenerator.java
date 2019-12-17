package com.datapath.kg.risks.api.export;

import com.datapath.kg.risks.api.dao.entity.ChecklistEntity;
import com.datapath.kg.risks.api.dao.service.ChecklistDAOService;
import com.datapath.kg.risks.api.exception.ChecklistExportException;
import com.datapath.kg.risks.api.export.header.HeaderPageEvent;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.datapath.kg.risks.api.Constants.COMPLETED_CHECKLIST_STATUS_ID;

@Service
public class ChecklistPdfGenerator {

    @Autowired
    private HeaderPageEvent headerPageEvent;
    @Autowired
    private ChecklistDAOService checklistDAO;
    @Autowired
    private ChecklistBuyerPdfGenerator buyerPdfGenerator;
    @Autowired
    private ChecklistTenderPdfGenerator tenderPdfGenerator;

    private static final Integer BUYER_TEMPLATE_TYPE_ID = 1;

    public byte[] pushDataToByteArray(Integer id) throws DocumentException, IOException {
        ChecklistEntity checklist = checklistDAO.getChecklist(id);

        if (!checklist.getStatus().getId().equals(COMPLETED_CHECKLIST_STATUS_ID)) {
            throw new ChecklistExportException("Can't export not completed checklist");
        }

        String templateType = checklist.getTemplateName();

        Document document = new Document(PageSize.A4, -65f, -65f, 35f, 35f);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);

        headerPageEvent.setTemplateType(templateType);
        pdfWriter.setPageEvent(headerPageEvent);
        document.open();

        if (BUYER_TEMPLATE_TYPE_ID.equals(checklist.getTemplateTypeId())) {
            buyerPdfGenerator.setWriter(pdfWriter);
            buyerPdfGenerator.generateBuyerReport(document, checklist);
        } else {
            tenderPdfGenerator.setWriter(pdfWriter);
            tenderPdfGenerator.generateTenderReport(document, checklist);
        }

        document.close();

        return outputStream.toByteArray();
    }
}
