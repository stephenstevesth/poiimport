package com.dsl.poiimport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

import java.util.Date;

public class QuickAccessCell {

    private ImportExcelFilter importExcelFilter;
    private Row row;

    public QuickAccessCell() {
    }

    public QuickAccessCell(ImportExcelFilter importExcelFilter, Row row) {
        this.importExcelFilter = importExcelFilter;
        this.row = row;
    }

    public ImportExcelFilter getImportExcelFilter() {
        return importExcelFilter;
    }

    public void setImportExcelFilter(ImportExcelFilter importExcelFilter) {
        this.importExcelFilter = importExcelFilter;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Cell getCell(String key){
        return row.getCell(importExcelFilter.getHeaderKey().get(key));
    }

    public String getString(String key){
//        row.getCell(importExcelFilter.getHeaderKey().get(key)).setCellType(CellType.STRING);
        if( row.getCell(importExcelFilter.getHeaderKey().get(key)).getCellType() != CellType.STRING ){
            if( row.getCell(importExcelFilter.getHeaderKey().get(key)).getCellType() == CellType.NUMERIC ){
                return row.getCell(importExcelFilter.getHeaderKey().get(key)).getNumericCellValue()+"";
            }else {
                return "";
            }
        }else {
            return row.getCell(importExcelFilter.getHeaderKey().get(key)).getStringCellValue();
        }
    }

    public String getStringIfNullEmpty(String key){
        if( importExcelFilter.getHeaderKey().get(key) == null ){
            return "";
        }else {
            if (row.getCell(importExcelFilter.getHeaderKey().get(key)) == null) {
                return "";
            } else {
                row.getCell(importExcelFilter.getHeaderKey().get(key)).setCellType(CellType.STRING);
                return row.getCell(importExcelFilter.getHeaderKey().get(key)).getStringCellValue();
            }
        }
    }

    public Date getDateIfNull(String key){
        if( importExcelFilter.getHeaderKey().get(key) == null ){
            return null;
        }else {
            if (row.getCell(importExcelFilter.getHeaderKey().get(key)) == null) {
                return null;
            } else {
//                row.getCell(importExcelFilter.getHeaderKey().get(key)).setCellType(CellType.NUMERIC);
                return row.getCell(importExcelFilter.getHeaderKey().get(key)).getDateCellValue();
            }
        }
    }

    public Double getNumeric(String key){
//        row.getCell(importExcelFilter.getHeaderKey().get(key)).setCellType(CellType.NUMERIC);
        return row.getCell(importExcelFilter.getHeaderKey().get(key)).getNumericCellValue();
    }

    public Date getDate(String key){
//        row.getCell(importExcelFilter.getHeaderKey().get(key)).setCellType(CellType.NUMERIC);
        return row.getCell(importExcelFilter.getHeaderKey().get(key)).getDateCellValue();
    }

    public boolean isEmpty(String key){
        boolean result = false;

        Integer keyIndex = importExcelFilter.getHeaderKey().get(key);

        if( keyIndex != null ) {
            Cell cell = row.getCell(keyIndex);
            if (cell != null) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty()) {
                        result = true;
                    }
                } else if (cell.getCellType() == CellType.ERROR) {
                    result = true;
                } else {
                    if (cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty() || cell.toString().trim().replaceAll("\u00A0", "").equalsIgnoreCase("")) {
                        result = true;
                    }
                }
            } else {
                return true;
            }
        }else{
            return true;
        }
        return result;
    }

}
