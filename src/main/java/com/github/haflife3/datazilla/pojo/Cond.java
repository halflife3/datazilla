package com.github.haflife3.datazilla.pojo;

/**
 * Created by maojianfeng on 2/25/17.
 */
public class Cond {
    private String fieldName;
    private String compareOpr;
    private Object value;

    public Cond() {
    }

    public Cond(String fieldName, String compareOpr, Object value) {
        this.fieldName = fieldName;
        this.compareOpr = compareOpr;
        this.value = value;
    }

    public Cond(String fieldName , Object value) {
        this.fieldName = fieldName;
        this.compareOpr = "=";
        this.value = value;
    }

    private Cond(Builder builder) {
        setFieldName(builder.fieldName);
        setCompareOpr(builder.compareOpr);
        setValue(builder.value);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getCompareOpr() {
        return compareOpr;
    }

    public void setCompareOpr(String compareOpr) {
        this.compareOpr = compareOpr;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cond{" +
                "fieldName='" + fieldName + '\'' +
                ", compareOpr='" + compareOpr + '\'' +
                ", value=" + value +
                '}';
    }

    public static final class Builder {
        private String fieldName;
        private String compareOpr;
        private Object value;

        public Builder() {
        }

        public Builder fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder compareOpr(String compareOpr) {
            this.compareOpr = compareOpr;
            return this;
        }

        public Builder value(Object value) {
            this.value = value;
            return this;
        }

        public Cond build() {
            return new Cond(this);
        }
    }
}
