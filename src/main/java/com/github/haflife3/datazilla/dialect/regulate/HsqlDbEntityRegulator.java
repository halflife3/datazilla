package com.github.haflife3.datazilla.dialect.regulate;

public class HsqlDbEntityRegulator implements EntityRegulator{
    @Override
    public String regulateTable(String table) {
        return table;
    }

    @Override
    public String regulateField(String field) {
        return field;
    }

    @Override
    public String simpleTable(String table){
        String simpleTable = table;
        if(table.contains(".")){
            String[] split = table.split("\\.");
            if(split.length==2){
                simpleTable = split[1];
            }
        }
        return simpleTable.toUpperCase();
    }
}
