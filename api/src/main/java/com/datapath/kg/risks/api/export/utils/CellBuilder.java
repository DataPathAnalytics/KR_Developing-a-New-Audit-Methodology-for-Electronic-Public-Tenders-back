package com.datapath.kg.risks.api.export.utils;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import static java.util.Objects.nonNull;

public class CellBuilder {

    public static PdfPCell build(Phrase phrase, CellSettings settings) {
        PdfPCell cell = new PdfPCell(phrase);

        if (nonNull(settings.getBorder())) {
            cell.setBorder(settings.getBorder());
        }

        if (nonNull(settings.getBackgroundColor())) {
            cell.setBackgroundColor(settings.getBackgroundColor());
        }

        if (nonNull(settings.getBorderColor())) {
            cell.setBorderColor(settings.getBorderColor());
        }

        if (nonNull(settings.getColspan())) {
            cell.setColspan(settings.getColspan());
        }

        if (nonNull(settings.getHorizontalAlignment())) {
            cell.setHorizontalAlignment(settings.getHorizontalAlignment());
        }

        if (nonNull(settings.getVerticalAlignment())) {
            cell.setVerticalAlignment(settings.getVerticalAlignment());
        }

        if (nonNull(settings.getPaddingLeft())) {
            cell.setPaddingLeft(settings.getPaddingLeft());
        }

        return cell;
    }
}
