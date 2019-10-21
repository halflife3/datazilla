package com.github.haflife3.datazilla.dialect;



import com.github.haflife3.datazilla.dialect.batch.BatchInserter;
import com.github.haflife3.datazilla.dialect.batch.DefaultBatchInserter;
import com.github.haflife3.datazilla.dialect.batch.MysqlBatchInserter;
import com.github.haflife3.datazilla.dialect.batch.PgBatchInserter;
import com.github.haflife3.datazilla.dialect.operator.DefaultOprStore;
import com.github.haflife3.datazilla.dialect.operator.MysqlOprStore;
import com.github.haflife3.datazilla.dialect.operator.OprStore;
import com.github.haflife3.datazilla.dialect.operator.PgOprStore;
import com.github.haflife3.datazilla.dialect.pagination.*;
import com.github.haflife3.datazilla.dialect.regulate.DefaultEntityRegulator;
import com.github.haflife3.datazilla.dialect.regulate.EntityRegulator;
import com.github.haflife3.datazilla.dialect.regulate.MysqlEntityRegulator;
import com.github.haflife3.datazilla.dialect.regulate.PgEntityRegulator;
import com.github.haflife3.datazilla.dialect.typemapping.MysqlTypeMapper;
import com.github.haflife3.datazilla.dialect.typemapping.PgTypeMapper;
import com.github.haflife3.datazilla.dialect.typemapping.TypeMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.github.haflife3.datazilla.dialect.DialectConst.MYSQL;
import static com.github.haflife3.datazilla.dialect.DialectConst.PG;
import static com.github.haflife3.datazilla.dialect.DialectConst.DEFAULT;


public class DialectFactory {
    public static List<String> SUPPORTED_DB = Arrays.asList(MYSQL,PG);

    private static Map<String, EntityRegulator> regulatorMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static Map<String, OprStore> oprStoreMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static Map<String, Pagination> paginationMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static Map<String, TypeMapper> typeMapperMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static Map<String, BatchInserter> batchInserterMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static Map<String, OfflinePagination> offlinePaginationMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    static {
        regulatorMap.put(DEFAULT,new DefaultEntityRegulator());
        regulatorMap.put(MYSQL,new MysqlEntityRegulator());
        regulatorMap.put(PG,new PgEntityRegulator());

        oprStoreMap.put(DEFAULT,new DefaultOprStore());
        oprStoreMap.put(MYSQL,new MysqlOprStore());
        oprStoreMap.put(PG,new PgOprStore());

        paginationMap.put(DEFAULT,new DefaultPagination());
        paginationMap.put(MYSQL,new MysqlPagination());
        paginationMap.put(PG,new PgPagination());

        typeMapperMap.put(MYSQL,new MysqlTypeMapper());
        typeMapperMap.put(PG,new PgTypeMapper());

        batchInserterMap.put(DEFAULT,new DefaultBatchInserter());
        batchInserterMap.put(MYSQL,new MysqlBatchInserter());
        batchInserterMap.put(PG,new PgBatchInserter());

        offlinePaginationMap.put(DEFAULT, new DefaultOfflinePagination());
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
