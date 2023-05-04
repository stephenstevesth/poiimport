package com.dsl.poiimport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ImportExcelFilter {

    private final Map<String,ExcelHeader> requiredHeader;

    private final Map<String, Integer> headerKey;

    private final ImportFilterHelper importFilterHelper;

    private Integer lastRowNum;

    public ImportExcelFilter(ImportFilterHelper importFilterHelper){
        this.requiredHeader = new HashMap<>();
        this.headerKey = new HashMap<>();
        this.importFilterHelper = importFilterHelper;
    }

    public Map<String, ExcelHeader> getRequiredHeader() {
        return requiredHeader;
    }

    public Map<String, Integer> getHeaderKey() {
        return headerKey;
    }

    public ImportFilterHelper getImportFilterHelper() {
        return importFilterHelper;
    }

    public int getColumnValidSize(){
        int size = -1;

        if( getRequiredHeader().size() > 0 ) {
            size = 0;

            for(Map.Entry<String,ExcelHeader> e: getRequiredHeader().entrySet()){

                if( e.getValue().isRequired() ){
                    size++;
                }

            }
        }

        return size;
    }

    public void setUpRequiredHeader(List<ExcelHeader> headers){
        headers.forEach(h->{
            if( !h.isDisplayOnly() ) {
                getRequiredHeader().put(h.getKey(), h);
            }
        });
    }

    public void setUpHeaderKey(Sheet sheet){
        Row firstRow = sheet.getRow(sheet.getFirstRowNum());
        if( !getRequiredHeader().isEmpty() && firstRow != null ) {
            Cell cell;
            Iterator<Cell> cells = firstRow.cellIterator();
            while (cells.hasNext()) {
                cell = cells.next();

                String temp = cell.toString().trim();

                for(ExcelHeader h:getRequiredHeader().values()){
//                    if( !h.isDisplayOnly() ) {
                        for (String alias : h.getAlias()) {
                            if (temp.equalsIgnoreCase(alias)) {
                                getHeaderKey().put(h.getKey(), cell.getColumnIndex());
                                break;
                            }
                        }
//                    }
                }

            }

            lastRowNum = sheet.getLastRowNum()+1;

        }

    }

    public boolean checkHeaderExist(UploadResult ur){
        boolean valid = true;

        int rowNum = 1;
        int emptyCounter = 0;
        StringBuilder msg = new StringBuilder();
        ImportErrorMessage iem = new ImportErrorMessage();
        iem.setNo(rowNum);

        for(ExcelHeader h:getRequiredHeader().values()){
//            if( !h.isDisplayOnly() ) {
                emptyCounter += getImportFilterHelper().filterHeaderNotExist(getHeaderKey().get(h.getKey()), h.getName(), msg);
//            }
        }

        if( emptyCounter != getHeaderKey().size() ){
            if( msg.length() > 0 ){
                msg.append(getImportFilterHelper().append(" Silakan Mengikuti Format File Contoh.", msg.length()));

                iem.setMsg(msg.toString());
                ur.getFailedMessage().put(rowNum,iem);
                valid = false;
            }else{
                if( lastRowNum < 2 ){
                    msg.append(getImportFilterHelper().append("Tidak Ada Data Untuk Diunggah.", msg.length()));
                    iem.setNo(rowNum+1);
                    iem.setMsg(msg.toString());
                    ur.getFailedMessage().put(rowNum+1,iem);
                    valid = false;
                }
            }
        }else{
            valid = false;
        }

        return valid;
    }

    private boolean checkRowExist(Row row){
        boolean valid = true;
        int emptyCounter = 0;
        for(ExcelHeader h:getRequiredHeader().values()) {
            Cell cell = row.getCell(getHeaderKey().get(h.getKey()));
            if( h.isRequired() ) {
//                if (cell == null || cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty() || cell.toString().trim().replaceAll("\u00A0", "").equalsIgnoreCase("")) {
                if (cell == null || cell.getCellType() == CellType.BLANK || cell.toString().trim().isEmpty() || cell.toString().trim().replaceAll("\u00A0", "").equalsIgnoreCase("")) {
                    emptyCounter++;
                }
            }
        }

        if( emptyCounter == getColumnValidSize() ){
            valid = false;
        }

        return valid;
    }

    private boolean checkColumnExist(Row row, UploadResult ur){
        boolean valid = true;
        int emptyCounter = 0;
        StringBuilder msg = new StringBuilder();
        ImportErrorMessage iem = new ImportErrorMessage();
        iem.setNo(row.getRowNum()+1);

        for(ExcelHeader h:getRequiredHeader().values()){

            switch (h.getType().toLowerCase()){
                case "string":
                    if( h.isRequired() ) {
                        emptyCounter += getImportFilterHelper().filterColumnStringNotExist(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                    }else{
                        if( row.getCell(getHeaderKey().get(h.getKey())) != null && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.BLANK && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.ERROR && !row.getCell(getHeaderKey().get(h.getKey())).toString().equalsIgnoreCase("") ) {
//                            row.getCell(getHeaderKey().get(h.getKey())).setCellType(CellType.STRING);
                            getImportFilterHelper().filterColumnStringNotExist(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                        }
                    }
                    break;
                case "note":
                    if( h.isRequired() ) {
                        emptyCounter += getImportFilterHelper().fillColumnNoteIfEmpty(row, getHeaderKey().get(h.getKey()));
                    }else{
                        getImportFilterHelper().fillColumnNoteIfEmpty(row, getHeaderKey().get(h.getKey()));
                    }
                    break;
                case "date":
                    if( h.isRequired() ) {
                        emptyCounter += getImportFilterHelper().filterColumnDateNotExist(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                    }else{
                        if( row.getCell(getHeaderKey().get(h.getKey())) != null && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.BLANK && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.ERROR && !row.getCell(getHeaderKey().get(h.getKey())).toString().equalsIgnoreCase("")){
//                            row.getCell(getHeaderKey().get(h.getKey())).setCellType(CellType.NUMERIC);
                            getImportFilterHelper().filterColumnDateNotExist(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                        }
                    }
                    break;
                case "time":
                    if( h.isRequired() ) {
                        emptyCounter += getImportFilterHelper().filterColumnDateNotExist(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                    }else{
                        if( row.getCell(getHeaderKey().get(h.getKey())) != null && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.BLANK && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.ERROR && !row.getCell(getHeaderKey().get(h.getKey())).toString().equalsIgnoreCase("")) {
//                            row.getCell(getHeaderKey().get(h.getKey())).setCellType(CellType.NUMERIC);
                            getImportFilterHelper().filterColumnDateNotExist(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                        }
                    }
                    break;
                case "number":
                    if( h.isRequired() ) {
                        emptyCounter += getImportFilterHelper().filterColumnNumberNotExist(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                    }else{
                        if( row.getCell(getHeaderKey().get(h.getKey())) != null && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.BLANK && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.ERROR && !row.getCell(getHeaderKey().get(h.getKey())).toString().equalsIgnoreCase("") ) {
//                            row.getCell(getHeaderKey().get(h.getKey())).setCellType(CellType.NUMERIC);
                            getImportFilterHelper().filterColumnNumberNotExist(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                        }
                    }
                    break;
            }

        }

        if( emptyCounter != getColumnValidSize() ){
            if( msg.length() > 0 ){
                iem.setMsg(msg+" Pada Baris "+iem.getNo());
                ur.getFailedMessage().put(row.getRowNum()+1,iem);
                valid = false;
            }
        }else{
            valid = false;
        }

        return valid;
    }

    private boolean checkFormatValid(Row row, UploadResult ur){
        boolean valid = true;
        StringBuilder msg = new StringBuilder();
        int no = row.getRowNum()+1;

        for(ExcelHeader h:getRequiredHeader().values()){

            switch (h.getType().toLowerCase()){
                case "string":
                    if( h.isRequired() ) {
                        getImportFilterHelper().filterColumnStringNotValid(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                    }else{
                        if( row.getCell(getHeaderKey().get(h.getKey())) != null && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.BLANK ) {
                            getImportFilterHelper().filterColumnStringNotValid(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                        }
                    }
                    break;
                case "note":
                    if( h.isRequired() ) {
                        getImportFilterHelper().filterColumnStringNotValid(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                    }
                    break;
                case "date":
                    if( h.isRequired() ) {
                        getImportFilterHelper().filterColumnDateNotValid(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                    }else{
                        if( row.getCell(getHeaderKey().get(h.getKey())) != null && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.BLANK ){
                            getImportFilterHelper().filterColumnDateNotValid(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                        }
                    }
                    break;
                case "time":

                        if (row.getCell(getHeaderKey().get(h.getKey())).getCellType() == CellType.ERROR) {
                            msg.append(getImportFilterHelper().append("Kolom " + h.getName() + " Data Tidak Valid", msg.length()));
                        } else {
                            if (row.getCell(getHeaderKey().get(h.getKey())).getCellType() == CellType.NUMERIC) {
                                DateTime d = new DateTime(row.getCell(getHeaderKey().get(h.getKey())).getDateCellValue());
                                DateTime dt = new DateTime().withHourOfDay(d.hourOfDay().get()).withMinuteOfHour(d.minuteOfHour().get());

                                row.getCell(getHeaderKey().get(h.getKey())).setCellValue(dt.toString("HH:mm"));
                            }
                        }

                    break;
                case "number":
                    if( h.isRequired() ) {
                        getImportFilterHelper().filterColumnNumberNotValid(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                    }else{
                        if( row.getCell(getHeaderKey().get(h.getKey())) != null && row.getCell(getHeaderKey().get(h.getKey())).getCellType() != CellType.BLANK ){
                            getImportFilterHelper().filterColumnNumberNotValid(row.getCell(getHeaderKey().get(h.getKey())), h.getName(), msg);
                        }
                    }
                    break;
            }

        }

        if( msg.length() > 0 ){
            ImportErrorMessage iem = new ImportErrorMessage();
            iem.setNo(no);

            if( ur.getFailedMessage().get(no) != null ){
                iem = (ImportErrorMessage) ur.getFailedMessage().get(no);
            }
            if( iem.getMsg().length() > 1 ){
                iem.setMsg(iem.getMsg()+", "+msg);
            }else{
                iem.setMsg(msg.toString());
            }
            ur.getFailedMessage().put(no,iem);

            valid = false;
        }
        return valid;
    }

    public boolean checkColumnAndFormat(Row row,UploadResult ur){
        boolean valid = checkRowExist(row);

        if( valid ) {
            valid = checkColumnExist(row, ur);

            if (valid) {
                valid = checkFormatValid(row, ur);
            }
        }

        return valid;
    }

}
