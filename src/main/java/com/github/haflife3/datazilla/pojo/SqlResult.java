package com.github.haflife3.datazilla.pojo;

import java.util.List;

/**
 * Created by maojianfeng on 2/25/17.
 */
public class SqlResult {
    private String sql;
    private List<Object> values;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }
}
