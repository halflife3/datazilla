package com.github.haflife3.datazilla.dialect.typemapping;

import java.util.Map;
import java.util.TreeMap;

public class PgTypeMapper implements TypeMapper {
    @Override
    public Map<String, String> getTypeMap() {
        Map<String,String> typeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        typeMap.put("BIGINT","Long");
        typeMap.put("int8","Long");
        typeMap.put("smallint","Integer");
        typeMap.put("int2","Integer");
        typeMap.put("integer","Integer");
        typeMap.put("int","Integer");
        typeMap.put("int4","Integer");
        typeMap.put("decimal","Double");
        typeMap.put("numeric","Double");
        typeMap.put("real","Double");
        typeMap.put("float4","Double");
        typeMap.put("double precision","Double");
        typeMap.put("float8","Double");
        typeMap.put("smallserial","Integer");
        typeMap.put("serial2","Integer");
        typeMap.put("serial","Integer");
        typeMap.put("serial4","Integer");
        typeMap.put("bigserial","Long");
        typeMap.put("serial8","Long");
//        typeMap.put("money","java.math.BigDecimal");
        typeMap.put("character varying","String");
        typeMap.put("BPCHAR","String");
        typeMap.put("varchar","String");
        typeMap.put("character","String");
        typeMap.put("char","String");
        typeMap.put("text","String");
        typeMap.put("TIMESTAMP","java.sql.Timestamp");
        typeMap.put("timestamptz","java.sql.Timestamp");
        typeMap.put("DATE","java.sql.Date");
        typeMap.put("TIME","java.sql.Time");
        typeMap.put("timetz","java.sql.Date");
        typeMap.put("boolean","Boolean");
        typeMap.put("bool","Boolean");
        return typeMap;
    }
}
