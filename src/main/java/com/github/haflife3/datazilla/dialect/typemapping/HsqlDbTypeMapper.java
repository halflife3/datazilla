package com.github.haflife3.datazilla.dialect.typemapping;

import java.util.Map;
import java.util.TreeMap;

public class HsqlDbTypeMapper implements TypeMapper {
    @Override
    public Map<String, String> getTypeMap() {
        Map<String,String> typeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        typeMap.put("TINYINT","Integer");
        typeMap.put("SMALLINT","Integer");
        typeMap.put("INTEGER","Integer");

        typeMap.put("BOOLEAN","Boolean");

        typeMap.put("BIGINT","Long");

        typeMap.put("REAL","Double");
        typeMap.put("FLOAT","Double");
        typeMap.put("DOUBLE","Double");
        typeMap.put("DECIMAL","Double");
        typeMap.put("NUMERIC","Double");

        typeMap.put("DATETIME","java.util.Date");
        typeMap.put("TIMESTAMP","java.util.Date");
        typeMap.put("DATE","java.util.Date");
        typeMap.put("TIME","java.util.Date");

        typeMap.put("CHAR","String");
        typeMap.put("CHARACTER","String");
        typeMap.put("VARCHAR","String");
        typeMap.put("LONGVARCHAR","String");
        typeMap.put("LONGTEXT","String");

        typeMap.put("UUID","java.util.UUID");
        return typeMap;
    }
}
