package com.github.haflife3.datazilla.dialect.regulate;

import com.github.haflife3.datazilla.dialect.DialectConst;

public class HsqlDbEntityRegulator implements EntityRegulator{
    @Override
    public String getDatabaseType() {
        return DialectConst.HSQLDB;
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
