package com.github.haflife3.datazilla.dialect.typemapping;

import java.util.Map;
import java.util.TreeMap;

public class SqliteTypeMapper implements TypeMapper {
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
