package com.datapath.kg.risks.api.export.header;

import com.datapath.kg.risks.api.export.utils.PdfFont;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Setter
@Service
public class HeaderPageEvent extends PdfPageEventHelper {

    @Autowired
    private PdfFont font;

    private String templateType;

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(
                writer.getDirectContent(),
                Element.ALIGN_LEFT,
                new Phrase(templateType, font.getCalibri11ItalicFont()),
                20, document.getPageSize().getHeight() - 20, 0);

        PdfContentByte canvas = writer.getDirectContent();
        canvas.setColorStroke(new BaseColor(102, 153, 204));
        canvas.moveTo(7, document.getPageSize().getHeight() - 30);
        canvas.lineTo(document.getPageSize().getWidth() - 7, document.getPageSize().getHeight() - 30);
        canvas.closePathStroke();
    }
}
