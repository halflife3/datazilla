package com.github.haflife3.datazilla.dialect.regulate;

import org.apache.commons.lang3.StringUtils;

public interface EntityRegulator {
    String regulateTable(String table);
    String regulateField(String field);
    default boolean bareEntity(String entity){
        return StringUtils.isAlphanumeric(StringUtils.trimToEmpty(entity).replaceAll("_",""));
    }
    default String simpleTable(String table){
        return table;
    }
}
