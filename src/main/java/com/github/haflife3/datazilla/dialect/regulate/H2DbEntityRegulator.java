package com.github.haflife3.datazilla.dialect.regulate;

import com.github.haflife3.datazilla.dialect.DialectConst;

public class H2DbEntityRegulator implements EntityRegulator{
    @Override
    public String getDatabaseType() {
        return DialectConst.H2;
    }

    @Override
    public String simpleTable(String table){
        return table.toUpperCase();
    }
}
