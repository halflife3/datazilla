package com.github.haflife3.datazilla.dialect.typemapping;

import java.util.Map;
import java.util.TreeMap;

public class H2TypeMapper implements TypeMapper {
    @Override
    public Map<String, String> getTypeMap() {
        Map<String,String> typeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        typeMap.put("INT","Integer");
        typeMap.put("INTEGER","Integer");
        typeMap.put("MEDIUMINT","Integer");
        typeMap.put("INT4","Integer");
        typeMap.put("SIGNED","Integer");
        typeMap.put("ENUM","Integer");

        typeMap.put("BOOLEAN","Boolean");
        typeMap.put("BIT","Boolean");
        typeMap.put("BOOL","Boolean");

        typeMap.put("TINYINT","Byte");

        typeMap.put("SMALLINT","Short");
        typeMap.put("INT2","Short");
        typeMap.put("YEAR","Short");

        typeMap.put("BIGINT","Long");
        typeMap.put("INT8","Long");
        typeMap.put("IDENTITY","Long");

        typeMap.put("DECIMAL","java.math.BigDecimal");
        typeMap.put("NUMBER","java.math.BigDecimal");
        typeMap.put("DEC","java.math.BigDecimal");
        typeMap.put("NUMERIC","java.math.BigDecimal");

        typeMap.put("DOUBLE","Double");
        typeMap.put("FLOAT","Double");
        typeMap.put("FLOAT8","Double");
        typeMap.put("REAL","Double");
        typeMap.put("FLOAT4","Double");

        typeMap.put("TIME","java.util.Date");
        typeMap.put("DATE","java.util.Date");
        typeMap.put("TIMESTAMP","java.util.Date");
        typeMap.put("DATETIME","java.util.Date");
        typeMap.put("SMALLDATETIME","java.util.Date");

        typeMap.put("OTHER","Object");

        typeMap.put("VARCHAR","String");
        typeMap.put("CHARACTER VARYING","String");
        typeMap.put("LONGVARCHAR","String");
        typeMap.put("VARCHAR2","String");
        typeMap.put("NVARCHAR","String");
        typeMap.put("NVARCHAR2","String");
        typeMap.put("VARCHAR_CASESENSITIVE","String");
        typeMap.put("VARCHAR_IGNORECASE","String");
        typeMap.put("CHAR","String");
        typeMap.put("CHARACTER","String");
        typeMap.put("NCHAR","String");

        typeMap.put("UUID","java.util.UUID");
        return typeMap;
    }
}
