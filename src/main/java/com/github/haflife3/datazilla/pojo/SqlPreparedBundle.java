package com.github.haflife3.datazilla.pojo;

import java.util.Arrays;

public class SqlPreparedBundle {
    private String sql;
    private Object[] values;

    public SqlPreparedBundle() {
    }

    public SqlPreparedBundle(String sql, Object[] values) {
        this.sql = sql;
        this.values = values;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "SqlPreparedBundle{" +
                "sql='" + sql + '\'' +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
