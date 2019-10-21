package com.github.haflife3.datazilla.pojo;

/**
 * Created by maojianfeng on 2/17/17.
 */
public class FieldValuePair {
    private String field;
    private Object value;

    public FieldValuePair() {
    }

    public FieldValuePair(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
