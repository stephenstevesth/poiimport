package com.dsl.poiimport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVHeader {

    private String key;
    private String name;
    private List<String> alias;
    private String type;
    private boolean required;

    public CSVHeader() {
    }

    public CSVHeader(String key, String name, String type) {
        this(key,name,type,true);
    }

    public CSVHeader(String key, String name, String type, boolean required) {
        this(key,name,new ArrayList<>(Collections.singletonList(name)),type,required);
    }

    public CSVHeader(String key, String name, List<String> alias, String type) {
        this(key,name,alias,type,true);
    }

    public CSVHeader(String key, String name, List<String> alias, String type, boolean required) {
        this.key = key;
        this.name = name;
        this.alias = alias;
        this.type = type;
        this.required = required;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

}
