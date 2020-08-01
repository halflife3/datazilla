package com.github.haflife3.datazilla.dialect.regulate;

public interface EntityRegulator {
    default String simpleTable(String table){
        return table;
    }
}
