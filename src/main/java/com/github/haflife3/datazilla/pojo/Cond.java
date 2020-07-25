package com.github.haflife3.datazilla.pojo;

/**
 * Created by maojianfeng on 2/25/17.
 */
public class Cond {
    private String columnName;
    private String compareOpr;
    private Object value;

    public Cond() {
    }

    public Cond(String columnName, String compareOpr, Object value) {
        this.columnName = columnName;
        this.compareOpr = compareOpr;
        this.value = value;
    }

    public Cond(String columnName, Object value) {
        this.columnName = columnName;
        this.compareOpr = "=";
        this.value = value;
    }

    private Cond(Builder builder) {
        setColumnName(builder.columnName);
        setCompareOpr(builder.compareOpr);
        setValue(builder.value);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
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
                "columnName='" + columnName + '\'' +
                ", compareOpr='" + compareOpr + '\'' +
                ", value=" + value +
                '}';
    }

    public static final class Builder {
        private String columnName;
        private String compareOpr;
        private Object value;

        public Builder() {
        }

        public Builder columnName(String columnName) {
            this.columnName = columnName;
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
