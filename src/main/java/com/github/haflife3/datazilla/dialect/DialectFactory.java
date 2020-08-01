package com.github.haflife3.datazilla.dialect;


import com.github.haflife3.datazilla.dialect.regulate.*;
import com.github.haflife3.datazilla.dialect.typemapping.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.github.haflife3.datazilla.dialect.DialectConst.*;


public class DialectFactory {
    public static List<String> SUPPORTED_DB = Arrays.asList(MYSQL,PG,H2,SQLITE,HSQLDB,MSSQL);

    private static final Map<String, EntityRegulator> regulatorMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<String, TypeMapper> typeMapperMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        regulatorMap.put(DEFAULT,new DefaultEntityRegulator());
        regulatorMap.put(MYSQL,new MysqlEntityRegulator());
        regulatorMap.put(PG,new PgEntityRegulator());
        regulatorMap.put(H2,new DefaultEntityRegulator());
        regulatorMap.put(SQLITE,new DefaultEntityRegulator());
        regulatorMap.put(HSQLDB,new HsqlDbEntityRegulator());
        regulatorMap.put(MSSQL,new DefaultEntityRegulator());

        typeMapperMap.put(MYSQL,new MysqlTypeMapper());
        typeMapperMap.put(PG,new PgTypeMapper());
        typeMapperMap.put(H2,new H2TypeMapper());
        typeMapperMap.put(SQLITE,new SqliteTypeMapper());
        typeMapperMap.put(HSQLDB,new HsqlDbTypeMapper());
        typeMapperMap.put(MSSQL,new MsSqlTypeMapper());
    }

    public static EntityRegulator getEntityRegulator(String dbType){
        return regulatorMap.get(dbType);
    }
    public static TypeMapper getTypeMapper(String dbType){
        return typeMapperMap.get(dbType);
    }
}
