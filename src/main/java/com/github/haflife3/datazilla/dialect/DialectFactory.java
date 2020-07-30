package com.github.haflife3.datazilla.dialect;


import com.github.haflife3.datazilla.dialect.batch.*;
import com.github.haflife3.datazilla.dialect.operator.*;
import com.github.haflife3.datazilla.dialect.pagination.*;
import com.github.haflife3.datazilla.dialect.regulate.*;
import com.github.haflife3.datazilla.dialect.typemapping.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.github.haflife3.datazilla.dialect.DialectConst.*;


public class DialectFactory {
    public static List<String> SUPPORTED_DB = Arrays.asList(MYSQL,PG,H2,SQLITE,HSQLDB);

    private static final Map<String, EntityRegulator> regulatorMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<String, OprStore> oprStoreMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<String, Pagination> paginationMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<String, TypeMapper> typeMapperMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<String, BatchInserter> batchInserterMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Map<String, OfflinePagination> offlinePaginationMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        regulatorMap.put(DEFAULT,new DefaultEntityRegulator());
        regulatorMap.put(MYSQL,new MysqlEntityRegulator());
        regulatorMap.put(PG,new PgEntityRegulator());
        regulatorMap.put(H2,new DefaultEntityRegulator());
        regulatorMap.put(SQLITE,new DefaultEntityRegulator());
        regulatorMap.put(HSQLDB,new HsqlDbEntityRegulator());

        oprStoreMap.put(DEFAULT,new DefaultOprStore());
        oprStoreMap.put(MYSQL,new MysqlOprStore());
        oprStoreMap.put(PG,new PgOprStore());
        oprStoreMap.put(H2,new H2OprStore());
        oprStoreMap.put(SQLITE,new SqliteOprStore());
        oprStoreMap.put(HSQLDB,new HsqlDbOprStore());

        paginationMap.put(DEFAULT,new DefaultPagination());
        paginationMap.put(MYSQL,new MysqlPagination());
        paginationMap.put(PG,new PgPagination());
        paginationMap.put(H2,new H2Pagination());
        paginationMap.put(SQLITE,new SqlitePagination());
        paginationMap.put(HSQLDB,new HsqlDbPagination());

        typeMapperMap.put(MYSQL,new MysqlTypeMapper());
        typeMapperMap.put(PG,new PgTypeMapper());
        typeMapperMap.put(H2,new H2TypeMapper());
        typeMapperMap.put(SQLITE,new SqliteTypeMapper());
        typeMapperMap.put(HSQLDB,new HsqlDbTypeMapper());

        batchInserterMap.put(DEFAULT,new DefaultBatchInserter());
        batchInserterMap.put(MYSQL,new MysqlBatchInserter());
        batchInserterMap.put(PG,new PgBatchInserter());
        batchInserterMap.put(H2,new DefaultBatchInserter());
        batchInserterMap.put(SQLITE,new SqliteBatchInserter());
        batchInserterMap.put(HSQLDB,new HsqlDbBatchInserter());

        offlinePaginationMap.put(DEFAULT, new DefaultOfflinePagination());
        offlinePaginationMap.put(MYSQL, new DummyOfflinePagination());
        offlinePaginationMap.put(PG, new DummyOfflinePagination());
        offlinePaginationMap.put(H2, new DummyOfflinePagination());
        offlinePaginationMap.put(SQLITE, new DummyOfflinePagination());
        offlinePaginationMap.put(HSQLDB, new DummyOfflinePagination());
    }

    public static EntityRegulator getEntityRegulator(String dbType){
        return regulatorMap.get(dbType);
    }
    public static OprStore getOprStore(String dbType){
        return oprStoreMap.get(dbType);
    }
    public static Pagination getPagination(String dbType){
        return paginationMap.get(dbType);
    }
    public static TypeMapper getTypeMapper(String dbType){
        return typeMapperMap.get(dbType);
    }
    public static BatchInserter getBatchInserter(String dbType){
        return batchInserterMap.get(dbType);
    }
    public static OfflinePagination getOfflinePagination(String dbType){
        return offlinePaginationMap.get(dbType);
    }
}
