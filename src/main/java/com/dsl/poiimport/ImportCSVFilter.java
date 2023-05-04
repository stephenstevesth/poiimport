package com.dsl.poiimport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportCSVFilter {

    private final Map<String,CSVHeader> requiredHeader;

    private final Map<String, Integer> headerKey;

    private final ImportCSVFilterHelper importCSVFilterHelper;

    private final String delimiter;

    public ImportCSVFilter(ImportCSVFilterHelper importCSVFilterHelper){
        this.requiredHeader = new HashMap<>();
        this.headerKey = new HashMap<>();
        this.importCSVFilterHelper = importCSVFilterHelper;
        this.delimiter = ",";
    }

    public ImportCSVFilter(ImportCSVFilterHelper importCSVFilterHelper,String delimiter){
        this.requiredHeader = new HashMap<>();
        this.headerKey = new HashMap<>();
        this.importCSVFilterHelper = importCSVFilterHelper;
        this.delimiter = delimiter;
    }

    public Map<String, CSVHeader> getRequiredHeader() {
        return requiredHeader;
    }

    public Map<String, Integer> getHeaderKey() {
        return headerKey;
    }

    public ImportCSVFilterHelper getImportFilterHelper() {
        return importCSVFilterHelper;
    }

    public int getColumnValidSize(){
        int size = -1;

        if( getRequiredHeader().size() > 0 ) {
            size = 0;

            for(Map.Entry<String,CSVHeader> e: getRequiredHeader().entrySet()){

                if( e.getValue().isRequired() ){
                    size++;
                }

            }
        }

        return size;
    }

    public void setUpRequiredHeader(List<CSVHeader> headers){
        headers.forEach(h-> getRequiredHeader().put(h.getKey(),h));
    }

    public void setUpHeaderKey(InputStream sheet) throws Exception{
        if( !getRequiredHeader().isEmpty() ) {

            try (BufferedReader br = new BufferedReader(new InputStreamReader(sheet, StandardCharsets.UTF_8))) {
                String[] firstRow = br.readLine().split(delimiter);
                for(int i=0;i<firstRow.length;i++){
                    String temp = firstRow[i];

                    if( temp != null ) {

                        temp = temp.trim();

                        for (CSVHeader h : getRequiredHeader().values()) {

                            for (String alias : h.getAlias()) {
                                if (temp.equalsIgnoreCase(alias)) {
                                    getHeaderKey().put(h.getKey(), i);
                                    break;
                                }
                            }

                        }
                    }
                }
            }

        }

    }

    public boolean checkHeaderExist(UploadResult ur){
        boolean valid = true;

        int rowNum = 1;
        int emptyCounter = 0;
        StringBuilder msg = new StringBuilder();
        ImportErrorMessage iem = new ImportErrorMessage();
        iem.setNo(rowNum);

        for(CSVHeader h:getRequiredHeader().values()){

            emptyCounter += getImportFilterHelper().filterHeaderNotExist(getHeaderKey().get(h.getKey()),h.getName(),msg);

        }

        if( emptyCounter != getHeaderKey().size() ){
            if( msg.length() > 0 ){
                msg.append(getImportFilterHelper().append(" Silakan Mengikuti Format File Contoh.", msg.length()));

                iem.setMsg(msg.toString());
                ur.getFailedMessage().put(rowNum,iem);
                valid = false;
            }
        }else{
            valid = false;
        }

        return valid;
    }

    private boolean checkRowExist(String[] row){
        boolean valid = true;
        int emptyCounter = 0;
        for(CSVHeader h:getRequiredHeader().values()) {
            String cell = row[getHeaderKey().get(h.getKey())];
            if( h.isRequired() ) {
                if (cell == null || cell.trim().isEmpty() || cell.trim().replaceAll("\u00A0", "").equalsIgnoreCase("")) {
                    emptyCounter++;
                }
            }
        }

        if( emptyCounter == getColumnValidSize() ){
            valid = false;
        }

        return valid;
    }

    private boolean checkColumnExist(int rowNum,String[] row, UploadResult ur){
        boolean valid = true;
        int emptyCounter = 0;
        StringBuilder msg = new StringBuilder();
        ImportErrorMessage iem = new ImportErrorMessage();
        iem.setNo(rowNum);

        for(CSVHeader h:getRequiredHeader().values()){

            switch (h.getType().toLowerCase()){
                case "string":
                    if( h.isRequired() ) {
                        emptyCounter += getImportFilterHelper().filterColumnNotExist(row[getHeaderKey().get(h.getKey())], h.getName(), msg);
                    }else{
                        getImportFilterHelper().filterColumnNotExist(row[getHeaderKey().get(h.getKey())], h.getName(), msg);
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
                        emptyCounter += getImportFilterHelper().filterColumnNotExist(row[getHeaderKey().get(h.getKey())], h.getName(), msg);
                    }else{
                        getImportFilterHelper().filterColumnNotExist(row[getHeaderKey().get(h.getKey())], h.getName(), msg);
                    }
                    break;
                case "time":
                    if( h.isRequired() ) {
                        emptyCounter += getImportFilterHelper().filterColumnNotExist(row[getHeaderKey().get(h.getKey())], h.getName(), msg);
                    }else{
                        getImportFilterHelper().filterColumnNotExist(row[getHeaderKey().get(h.getKey())], h.getName(), msg);
                    }
                    break;
                case "number":
                    if( h.isRequired() ) {
                        emptyCounter += getImportFilterHelper().filterColumnNotExist(row[getHeaderKey().get(h.getKey())], h.getName(), msg);
                    }else{
                        getImportFilterHelper().filterColumnNotExist(row[getHeaderKey().get(h.getKey())], h.getName(), msg);
                    }
                    break;
            }

        }

        if( emptyCounter != getColumnValidSize() ){
            if( msg.length() > 0 ){
                iem.setMsg(msg+" Pada Baris "+iem.getNo());
                ur.getFailedMessage().put(rowNum,iem);
                valid = false;
            }
        }else{
            valid = false;
        }

        return valid;
    }

    public boolean checkColumnAndFormat(int rowNum,String[] row,UploadResult ur){
        boolean valid = checkRowExist(row);

        if( valid ) {
            valid = checkColumnExist(rowNum,row, ur);

//            if (valid) {
//                valid = checkFormatValid(row, ur);
//            }
        }

        return valid;
    }

}
