package com.dsl.poiimport;

import org.joda.time.DateTime;

import java.util.Date;

public class QuickAccessCSVCell {

    private ImportCSVFilter importCSVFilter;
    private String[] row;

    public QuickAccessCSVCell() {
    }

    public QuickAccessCSVCell(ImportCSVFilter importCSVFilter, String[] row) {
        this.importCSVFilter = importCSVFilter;
        this.row = row;
    }

    public ImportCSVFilter getImportExcelFilter() {
        return importCSVFilter;
    }

    public void setImportExcelFilter(ImportCSVFilter importCSVFilter) {
        this.importCSVFilter = importCSVFilter;
    }

    public String[] getRow() {
        return row;
    }

    public void setRow(String[] row) {
        this.row = row;
    }

    public String getCell(String key){
        return row[importCSVFilter.getHeaderKey().get(key)];
    }

    public String getString(String key){
        return row[importCSVFilter.getHeaderKey().get(key)];
    }

    public Double getNumeric(String key){
        return Double.valueOf(row[importCSVFilter.getHeaderKey().get(key)]);
    }

    public Date getDate(String key){
        return new DateTime(row[importCSVFilter.getHeaderKey().get(key)]).toDate();
    }

}
