package com.github.haflife3.datazilla.dialect.regulate;

import com.github.haflife3.datazilla.dialect.DialectConst;

public class PgEntityRegulator implements EntityRegulator{
    @Override
    public String getDatabaseType() {
        return DialectConst.PG;
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
        return simpleTable;
    }
}
