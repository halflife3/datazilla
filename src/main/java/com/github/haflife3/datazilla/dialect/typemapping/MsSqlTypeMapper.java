package com.github.haflife3.datazilla.dialect.typemapping;

import java.util.Map;
import java.util.TreeMap;

public class MsSqlTypeMapper implements TypeMapper {
    @Override
    public Map<String, String> getTypeMap() {
        Map<String,String> typeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        typeMap.put("bigint","Long");
        typeMap.put("BIGINT IDENTITY","Long");
        typeMap.put("bit","Boolean");
        typeMap.put("char","String");
        typeMap.put("date","java.util.Date");
        typeMap.put("datetime","java.util.Date");
        typeMap.put("decimal","Double");
        typeMap.put("float","Double");
        typeMap.put("int","Integer");
        typeMap.put("nchar","String");
        typeMap.put("ntext","String");
        typeMap.put("numeric","Double");
        typeMap.put("nvarchar","java.util.Date");
        typeMap.put("real","Double");
        typeMap.put("smalldatetime","java.util.Date");
        typeMap.put("smallint","Short");
        typeMap.put("text","String");
        typeMap.put("tinyint","Short");
        typeMap.put("uniqueidentifier","String");
        typeMap.put("varchar","String");
        return typeMap;
    }
}
