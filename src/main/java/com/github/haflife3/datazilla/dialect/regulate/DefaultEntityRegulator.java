package com.github.haflife3.datazilla.dialect.regulate;

public class DefaultEntityRegulator implements EntityRegulator{
    @Override
    public String regulateTable(String table) {
        return table;
    }

    @Override
    public String regulateField(String field) {
        return field;
    }
}
