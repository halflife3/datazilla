package com.github.haflife3.datazilla.dialect.typemapping;

import com.github.haflife3.datazilla.dialect.DialectConst;

import java.util.Map;
import java.util.TreeMap;

public class SqliteTypeMapper implements TypeMapper {
    @Override
    public String getDatabaseType() {
        return DialectConst.SQLITE;
    }
    @Override
    public Map<String, String> getTypeMap() {
        Map<String,String> typeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        typeMap.put("INTEGER","Long");
        typeMap.put("TEXT","String");
        typeMap.put("REAL","Double");
        typeMap.put("NUMERIC","Double");
        return typeMap;
    }
}
