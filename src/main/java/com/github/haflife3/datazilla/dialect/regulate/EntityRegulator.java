package com.github.haflife3.datazilla.dialect.regulate;

import com.github.haflife3.datazilla.dialect.DatabaseTypeMatcher;

public interface EntityRegulator extends DatabaseTypeMatcher {
    default String simpleTable(String table){
        return table;
    }
}
