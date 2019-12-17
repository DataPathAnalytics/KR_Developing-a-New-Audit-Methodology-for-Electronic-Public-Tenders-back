package com.datapath.kg.risks.api.dao;

import com.datapath.kg.risks.api.dto.ColumnDetailsDTO;
import org.apache.poi.ss.usermodel.*;
import org.springframework.jdbc.core.RowCountCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.datapath.kg.risks.api.Constants.*;
import static com.datapath.kg.risks.api.Utils.convertCase;
import static org.springframework.util.CollectionUtils.isEmpty;

public class PrioritizationExportHandler extends RowCountCallbackHandler {

    private static final short numberFormat = (short) BuiltinFormats.getBuiltinFormat("#,##0");
    private static final short doubleFormat = (short) BuiltinFormats.getBuiltinFormat("#,##0.00");

    private List<ColumnDetailsDTO> columnDetails;
    private List<Integer> selectedExportIds;
    private Sheet sheet;
    private int rowCount;

    public PrioritizationExportHandler(List<ColumnDetailsDTO> columnDetails, List<Integer> selectedExportIds, Sheet sheet) {
        this.columnDetails = columnDetails;
        this.sheet = sheet;
        this.selectedExportIds = selectedExportIds;
    }

    @Override
    protected void processRow(ResultSet rs, int rowNum) throws SQLException {
        if (!isEmpty(selectedExportIds) && !selectedExportIds.contains(rs.getInt(ID))) return;

        CellStyle numberStyle = sheet.getWorkbook().createCellStyle();
        CellStyle doubleStyle = sheet.getWorkbook().createCellStyle();

        numberStyle.setDataFormat(numberFormat);
        doubleStyle.setDataFormat(doubleFormat);

        Row row = sheet.createRow(++rowCount);
        for (int i = 0; i < columnDetails.size(); i++) {

            Cell cell = row.createCell(i);
            if (PROCUREMENT_METHOD_DETAILS_TITLE.equalsIgnoreCase(columnDetails.get(i).getName())) {
                cell.setCellValue(PROCUREMENT_METHOD_DETAILS_MAP.get(rs.getString(convertCase(columnDetails.get(i).getName()))));
            } else if (RISK_LEVEL_TITLE.equalsIgnoreCase(columnDetails.get(i).getName())) {
                cell.setCellValue(rs.getString(RISK_LEVEL_DESCRIPTION_COLUMN_NAME));
            } else if (CURRENT_STAGE_TITLE.equalsIgnoreCase(columnDetails.get(i).getName())) {
                cell.setCellValue(CURRENT_STAGE_MAP.get(rs.getString(convertCase(columnDetails.get(i).getName()))));
            } else if (PRIORITIZATION_PARAMETER_TITLE.equalsIgnoreCase(columnDetails.get(i).getName())) {
                cell.setCellValue(rs.getDouble(convertCase(columnDetails.get(i).getName())));
                cell.setCellStyle(doubleStyle);
            } else if (NUMBER_TYPE_FIELD_LIST.contains(columnDetails.get(i).getName())) {
                cell.setCellValue(rs.getLong(convertCase(columnDetails.get(i).getName())));
                cell.setCellStyle(numberStyle);
            } else if (CHECKLIST_STATUS_TITLE.equalsIgnoreCase(columnDetails.get(i).getName())) {
                cell.setCellValue(CHECKLIST_STATUS.get(rs.getInt(convertCase(columnDetails.get(i).getName()))));
            } else {
                cell.setCellValue(rs.getString(convertCase(columnDetails.get(i).getName())));
            }
        }
    }
}
