package com.dsl.poiimport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExcelHeader {

    private String key;
    private String name;
    private List<String> alias;
    private String type;
    private boolean required;
    private boolean displayOnly;

    public ExcelHeader() {
    }

    public ExcelHeader(String key, String name, String type) {
        this(key,name,type,true);
    }

    public ExcelHeader(String key, String name, String type, boolean required) {
        this(key,name,new ArrayList<>(Collections.singletonList(name)),type,required,false);
    }

    public ExcelHeader(String key, String name, String type, boolean required,boolean displayOnly) {
        this(key,name,new ArrayList<>(Collections.singletonList(name)),type,required,displayOnly);
    }

    public ExcelHeader(String key, String name, List<String> alias, String type) {
        this(key,name,alias,type,true,false);
    }

    public ExcelHeader(String key, String name, List<String> alias, String type, boolean required) {
        this(key,name,alias,type,required,false);
    }

    public ExcelHeader(String key, String name, List<String> alias, String type, boolean required,boolean displayOnly) {
        this.key = key;
        this.name = name;
        this.alias = alias;
        this.type = type;
        this.required = required;
        this.displayOnly = displayOnly;
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

    public boolean isDisplayOnly() {
        return displayOnly;
    }

    public void setDisplayOnly(boolean displayOnly) {
        this.displayOnly = displayOnly;
    }
}
