package com.dsl.poiimport;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportFilterHelper {

    public String append(String msg,int length){
        String extra = "";
        if( length > 1 ){ extra = ", "; }
        return extra + msg;
    }

    public String formatToYYYYMMDD(Date date){
        return new DateTime(date).toString("yyyy-MM-dd");
    }

    public String formatToYYYYMMDDHHMM(Date date){
        return new DateTime(date).toString("yyyy-MM-dd HH:mm");
    }

    public Date formatToDate(Date date,String time){

        DateTime dt1 = new DateTime(date);

        DateTimeFormatter dtFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");

        return dtFormatter.parseDateTime(dt1.toString("yyyy-MM-dd")+" "+time).toDate();
    }

    public boolean validateTime(final String time){
        String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
        Pattern pattern = Pattern.compile(TIME24HOURS_PATTERN);
        Matcher matcher = pattern.matcher(time);
        return matcher.matches();
    }

    public Integer filterHeaderNotExist(Integer column, String headerNameMessage, StringBuilder msg){
        int emptyCounter = 0;
        if ( column == null ) {
            msg.append(append("Kolom Header "+headerNameMessage+" Tidak Ada", msg.length()));
            emptyCounter++;
        }

        return emptyCounter;
    }

    public Integer filterColumnStringNotExist(Cell cell, String headerNameMessage, StringBuilder msg){
        int emptyCounter = 0;

//        if( cell == null || cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty() || cell.toString().trim().replaceAll("\u00A0", "").equalsIgnoreCase("") ){
        if( cell == null || cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty() || cell.toString().trim().replaceAll("\u00A0", "").equalsIgnoreCase("") ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Kosong",msg.length()));
            emptyCounter++;
        }

        return emptyCounter;
    }

    public Integer filterColumnNumberNotExist(Cell cell, String headerNameMessage, StringBuilder msg){
        int emptyCounter = 0;

//        if( cell == null || cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty() ){
        if( cell == null || cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty() ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Kosong",msg.length()));
            emptyCounter++;
        }

        return emptyCounter;
    }

    public Integer filterColumnDateNotExist(Cell cell, String headerNameMessage, StringBuilder msg){
        return filterColumnNumberNotExist(cell,headerNameMessage,msg);
    }

    public void filterColumnStringNotValid(Cell cell, String headerNameMessage, StringBuilder msg){

//        if( cell.getCellType() == CellType.ERROR || cell.getCellType() == CellType.FORMULA ){
        if( cell.getCellType() == CellType.ERROR || cell.getCellType() == CellType.FORMULA ){
            msg.append(append("Kolom "+headerNameMessage+" Data Tidak Valid",msg.length()));
        }else{
//            cell.setCellType(CellType.STRING);
            cell.setCellValue(cell.toString().trim().replaceAll("\u00A0", ""));
        }

    }

    public void filterColumnNumberNotValid(Cell cell, String headerNameMessage, StringBuilder msg){

//        if( cell.getCellType() != CellType.NUMERIC ){
        if( cell != null && cell.getCellType() != CellType.NUMERIC ){
            msg.append(append("Kolom "+headerNameMessage+" Format Data Harus Bentuk Numerik",msg.length()));
        }

    }

    public void filterColumnDateNotValid(Cell cell, String headerNameMessage, StringBuilder msg){
//        if( cell.getCellType() != CellType.NUMERIC ){
        if( cell != null && cell.getCellType() != CellType.NUMERIC ){
            msg.append(append("Kolom "+headerNameMessage+" Format Data Harus Bentuk Tanggal",msg.length()));
        }
    }

    public Integer fillColumnNoteIfEmpty(Row row, Integer no){
        int emptyCounter = 0;

//        if( row.getCell(no) == null || row.getCell(no).getCellType() == CellType.BLANK || row.getCell(no).toString().trim().isEmpty() || row.getCell(no).toString().trim().replaceAll("\u00A0", "").equalsIgnoreCase("") ){
        if( row.getCell(no) == null || row.getCell(no).getCellType() == CellType.BLANK || row.getCell(no).toString().trim().isEmpty() || row.getCell(no).toString().trim().replaceAll("\u00A0", "").equalsIgnoreCase("") ){
            row.createCell(no).setCellValue("");

            emptyCounter++;
        }

        return emptyCounter;
    }

    public void filterNotAFSOrHTM(String value,String headerNameMessage,Integer no,StringBuilder msg){
        if( !value.equalsIgnoreCase("AFS") && !value.equalsIgnoreCase("HTM") ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Sesuai Format AFS Atau HTM Pada Baris "+no,msg.length()));
        }
    }

    public void filterNotIDROrUSD(String value,String headerNameMessage,Integer no,StringBuilder msg){
        if( !value.equalsIgnoreCase("IDR") && !value.equalsIgnoreCase("USD") ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Sesuai Format IDR Atau USD Pada Baris "+no,msg.length()));
        }
    }

    public void filterNotSavingOrExtendOrWithdraw(String value,String headerNameMessage,Integer no,StringBuilder msg){
        if( !value.equalsIgnoreCase("Penempatan") && !value.equalsIgnoreCase("Perpanjangan") && !value.equalsIgnoreCase("Pencairan") ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Sesuai Format \"Penempatan\",\"Perpanjangan\" Atau \"Pencairan\" Pada Baris "+no,msg.length()));
        }
    }

    public void filterNotYesOrNo(String value,String headerNameMessage,Integer no,StringBuilder msg){
        if( !value.equalsIgnoreCase("Ya") && !value.equalsIgnoreCase("Tidak") ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Sesuai Format \"Ya\" Atau \"Tidak\" Pada Baris "+no,msg.length()));
        }
    }

    public void filterNumberNotLowerThanOrGreaterThan(Double value, Double lowerValue, Double greaterValue, String headerNameMessage,Integer no, StringBuilder msg){
        if( value > greaterValue ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Lebih Dari "+greaterValue+" Pada Baris "+no,msg.length()));
        }else if( value < lowerValue ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Lebih Kecil Dari "+lowerValue+" Pada Baris "+no,msg.length()));
        }
    }

    public void filterNumberNotLowerOrEqual(Double value, Double lowerValue, String headerNameMessage,Integer no, StringBuilder msg){
        if( value <= lowerValue ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Lebih Kecil Atau Sama Dengan "+lowerValue+" Pada Baris "+no,msg.length()));
        }
    }

    public void filterStringCommaNotGreaterThan(String text, String headerNameMessage,int greaterValue,Integer no, StringBuilder msg){
        if( text.length() > greaterValue ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Lebih Dari "+greaterValue+" Angka Di Belakang Koma Pada Baris "+no,msg.length()));
        }
    }

    public void filterStringNotGreaterThan(String text, String headerNameMessage,Integer no, StringBuilder msg){
        int greaterValue = 400;
        filterStringNotGreaterThan(text,headerNameMessage,greaterValue,no,msg);
    }

    public void filterStringNotGreaterThan(String text, String headerNameMessage,int greaterValue,Integer no, StringBuilder msg){
        if( text.length() > greaterValue ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Lebih Dari "+greaterValue+" Karakter Pada Baris "+no,msg.length()));
        }
    }

    public void filterStringNotLowerThanOrGreaterThan(String text, Integer lowerValue, Integer greaterValue, String headerNameMessage,Integer no, StringBuilder msg){
        if( text.length() > greaterValue ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Lebih Dari "+greaterValue+" Karakter Pada Baris "+no,msg.length()));
        }else if( text.length() < lowerValue ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Lebih Kecil Dari "+lowerValue+" Karakter Pada Baris "+no,msg.length()));
        }
    }

    public void filterDateNotGreaterThan(Date d1, Date d2, String headerNameMessage, String headerNameMessage2, Integer no, StringBuilder msg){
        if( d1.compareTo(d2) > 0 ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Lebih Besar Dari "+headerNameMessage2+" Pada Baris "+no,msg.length()));
        }
    }

    public void filterDateNotGreaterThanEqual(Date d1, Date d2, String headerNameMessage, String headerNameMessage2, Integer no, StringBuilder msg){
        if( d1.compareTo(d2) >= 0 ){
            msg.append(append("Kolom "+headerNameMessage+" Tidak Boleh Lebih Besar Sama Dengan "+headerNameMessage2+" Pada Baris "+no,msg.length()));
        }
    }

}
