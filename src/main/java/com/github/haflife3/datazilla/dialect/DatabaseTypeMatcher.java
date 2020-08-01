package com.github.haflife3.datazilla.dialect;

public interface DatabaseTypeMatcher {
    default String getDatabaseType(){
        return DialectConst.DEFAULT;
    }
    default boolean match(String databaseType){
        return databaseType.equalsIgnoreCase(getDatabaseType());
    }
}
