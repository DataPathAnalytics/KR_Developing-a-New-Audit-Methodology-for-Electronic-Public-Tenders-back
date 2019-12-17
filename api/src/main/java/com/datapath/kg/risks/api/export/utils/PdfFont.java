package com.datapath.kg.risks.api.export.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Data
public class PdfFont {

    private Font calibri11ItalicFont;
    private Font calibri9NormalFont;
    private Font calibri9BoldFont;
    private Font calibri11NormalFont;
    private Font calibri10ItalicFont;
    private Font calibri11BlueBoldFont;
    private Font calibri10BlueNormalFont;
    private Font calibri10NormalFont;
    private Font calibri12WhiteBoldFont;
    private Font calibri9WhiteBoldFont;
    private Font calibri11BlackBoldFont;
    private Font calibri10GreenBoldFont;

    public PdfFont() throws IOException, DocumentException {
        calibri11ItalicFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 11, Font.ITALIC);
        calibri9NormalFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9, Font.NORMAL);
        calibri9BoldFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9, Font.BOLD);
        calibri11NormalFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 11, Font.NORMAL);
        calibri10ItalicFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10, Font.ITALIC);
        calibri11BlueBoldFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 11, Font.BOLD, new BaseColor(102, 153, 204));
        calibri10BlueNormalFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10, Font.NORMAL, new BaseColor(102, 153, 204));
        calibri10NormalFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10, Font.NORMAL);
        calibri12WhiteBoldFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 12, Font.BOLD, BaseColor.WHITE);
        calibri9WhiteBoldFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9, Font.BOLD, BaseColor.WHITE);
        calibri11BlackBoldFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 11, Font.BOLD, BaseColor.BLACK);
        calibri10GreenBoldFont = new Font(BaseFont.createFont("/fonts/Calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 10, Font.NORMAL, new BaseColor(83,129,53));
    }
}
