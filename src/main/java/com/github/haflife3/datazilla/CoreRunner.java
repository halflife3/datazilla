package com.github.haflife3.datazilla;

import com.github.haflife3.datazilla.dialect.DialectConst;
import com.github.haflife3.datazilla.dialect.DialectFactory;
import com.github.haflife3.datazilla.dialect.batch.BatchInserter;
import com.github.haflife3.datazilla.dialect.batch.DefaultBatchInserter;
import com.github.haflife3.datazilla.dialect.pagination.*;
import com.github.haflife3.datazilla.logic.SqlBuilder;
import com.github.haflife3.datazilla.logic.TableObjectMetaCache;
import com.github.haflife3.datazilla.misc.*;
import com.github.haflife3.datazilla.pojo.QueryConditionBundle;
import com.github.haflife3.datazilla.pojo.SqlPreparedBundle;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class CoreRunner {
    private static final Logger logger = LoggerFactory.getLogger(CoreRunner.class);
    private final QueryRunner queryRunner;
    private String dbType;
    private final SqlBuilder sqlBuilder;
    private BatchInserter batchInserter;
    private Pagination pagination;
    private OfflinePagination offlinePagination;

    public CoreRunner(QueryRunner queryRunner) {
        this(queryRunner,null);
    }

    public CoreRunner(QueryRunner queryRunner,String dbType) {
        this.queryRunner = queryRunner;
        if(dbType==null){
            this.dbType = new PlatformUtils().determineDatabaseType(queryRunner.getDataSource());
            if(DialectFactory.SUPPORTED_DB.stream().noneMatch(this.dbType::equalsIgnoreCase)){
                this.dbType = DialectConst.DEFAULT;
            }
        }else {
            this.dbType = dbType;
        }
        initDialect();
        sqlBuilder = new SqlBuilder(this);
    }

    private void initDialect(){
        boolean batchInserterMatch = false;
        ServiceLoader<BatchInserter> batchInserters = ServiceLoader.load(BatchInserter.class);
        for (BatchInserter batchInserter : batchInserters) {
            if(batchInserter.match(dbType)){
                this.batchInserter = batchInserter;
                batchInserterMatch = true;
                break;
            }
        }
        if(!batchInserterMatch){
            this.batchInserter = new DefaultBatchInserter();
        }

        boolean paginationMatch = false;
        ServiceLoader<Pagination> paginations = ServiceLoader.load(Pagination.class);
        for (Pagination pagination : paginations) {
            if(pagination.match(dbType)){
                this.pagination = pagination;
                paginationMatch = true;
                break;
            }
        }
        if(!paginationMatch){
            this.pagination = new DefaultPagination();
        }

        boolean offlinePaginationMatch = false;
        ServiceLoader<OfflinePagination> offlinePaginations = ServiceLoader.load(OfflinePagination.class);
        for (OfflinePagination offlinePagination : offlinePaginations) {
            if(offlinePagination.match(dbType)){
                this.offlinePagination = offlinePagination;
                offlinePaginationMatch = true;
                break;
            }
        }
        if(!offlinePaginationMatch){
            if(!(this.pagination instanceof DefaultPagination)){
                this.offlinePagination = new DummyOfflinePagination();
            }else {
                this.offlinePagination = new DefaultOfflinePagination();
            }
        }
        logger.info("batchInserterMatch[{}],batchInserter[{}];paginationMatch[{}],pagination[{}];offlinePaginationMatch[{}],offlinePagination[{}]",batchInserterMatch,batchInserter,paginationMatch,pagination,offlinePaginationMatch,offlinePagination);
    }

    public String getDbType() {
        return dbType;
    }

    public DataSource getDataSource(){
        return queryRunner.getDataSource();
    }

    public QueryRunner getQueryRunner(){
        return queryRunner;
    }

    public BatchInserter getBatchInserter() {
        return batchInserter;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public OfflinePagination getOfflinePagination() {
        return offlinePagination;
    }

    public <T> List<T> genericQry(String sql, Class<T> clazz, Object[] values)  {
        TableObjectMetaCache.initTableObjectMeta(clazz,this);
        return genericQry(sql,new BeanListHandler<>(clazz,new BasicRowProcessor(MoreGenerousBeanProcessorFactory.populateBeanProcessor(clazz))),values);
    }

    public <T> List<T> genericQry(String sql, ResultSetHandler<List<T>> resultSetHandler, Object[] values)  {
        List<T> list;
        try {
            sql = getIdSql(sql);
            long start = System.currentTimeMillis();
            list = queryRunner.query(sql,resultSetHandler,values);
            long end = System.currentTimeMillis();
            log(sql,values,list,"RESULT-SIZE",(end-start));
        } catch (SQLException e) {
            throw new DBException(e);
        }finally {
            ExtraParamInjector.unsetSqlId();
        }
        return list;
    }

    public List<Map<String, Object>> genericQry(String sql, Object[] values){
        List<Map<String, Object>> list = null;
        try {
            sql = getIdSql(sql);
            long start = System.currentTimeMillis();
            list = queryRunner.query(sql,new MapListHandler(),values);
            long end = System.currentTimeMillis();
            log(sql,values,list,"RESULT-SIZE",(end-start));
        } catch (SQLException e) {
            throw new DBException(e);
        }finally {
            ExtraParamInjector.unsetSqlId();
        }
        return list;
    }

    public int genericUpdate(String sql, Object[] values) {
        int affected = 0;
        try {
            sql = getIdSql(sql);
            long start = System.currentTimeMillis();
            affected = queryRunner.update(sql, values);
            long end = System.currentTimeMillis();
            log(sql,values,affected,"AFFECTED",(end-start));
        } catch (SQLException e) {
            throw new DBException(e);
        }finally {
            ExtraParamInjector.unsetSqlId();
        }
        return affected;
    }
    
    public <T> List<T> genericQry(QueryConditionBundle qryCondition) {
        Class<?> resultClass = qryCondition.getResultClass();
        List<T> list = null;
        try {
            SqlPreparedBundle sqlPreparedBundle = sqlBuilder.composeSelect(qryCondition);
            String sql = sqlPreparedBundle.getSql();
            Object[] values = sqlPreparedBundle.getValues();
            list = genericQry(sql,(Class<T>)resultClass,values);
        } catch (Exception e) {
            throw new DBException(e);
        }
        return list;
    }
    
    public List<Map<String, Object>> genericMapQry(QueryConditionBundle qryCondition){
        SqlPreparedBundle sqlPreparedBundle = sqlBuilder.composeSelect(qryCondition);
        String sql = sqlPreparedBundle.getSql();
        Object[] values = sqlPreparedBundle.getValues();
        return genericQry(sql,values);
    }

    public int insert(String table, Map<String, Object> valueMap){
        int affectedNum = 0;
        String sql = "insert into "+table+" (";
        String valueSql = " values( ";
        List<Object> values = new ArrayList<>();
        for(Map.Entry<String, Object> entry:valueMap.entrySet()){
            String field = entry.getKey();
            Object value = entry.getValue();
            if(value!=null) {
                sql += field + ",";
                valueSql += "?,";
                values.add(value);
            }
        }
        sql = StringUtils.stripEnd(sql,",")+") ";
        valueSql = StringUtils.stripEnd(valueSql,",")+") ";
        sql = sql+valueSql;
        affectedNum = genericUpdate(sql,values.toArray());
        return affectedNum;
    }

    public <T> T insertWithReturn(String table, Map<String, Object> valueMap){
        long start = System.currentTimeMillis();
        T rt = null;
        try {
            String sql = "insert into "+table+" (";
            String valueSql = " values( ";
            List<Object> values = new ArrayList<>();
            for(Map.Entry<String, Object> entry:valueMap.entrySet()){
                String field = entry.getKey();
                Object value = entry.getValue();
                if(value!=null) {
                    sql += field + ",";
                    valueSql += "?,";
                    values.add(value);
                }
            }
            sql = StringUtils.stripEnd(sql,",")+") ";
            valueSql = StringUtils.stripEnd(valueSql,",")+") ";
            sql = sql+valueSql;
            sql = getIdSql(sql);
            Object[] valueArr = values.toArray();
            rt = queryRunner.insert(sql, new ScalarHandler<>(), valueArr);
            long end = System.currentTimeMillis();
            log(sql,valueArr,rt,"OUTPUT",(end-start));
        } catch (SQLException e) {
            throw new DBException(e);
        }finally {
            ExtraParamInjector.unsetSqlId();
        }
        return rt;
    }
    
    public int batchInsert(String table, List<Map<String, Object>> listMap){
        return batchInserter.batchInsert(this,table,listMap);
    }
    
    public List<String> getColNames(String table){
        List<String> cols = null;
        try {
            long start = System.currentTimeMillis();
            String sql = "select * from "+table+" where 1=2";
            sql = getIdSql(sql);
            cols = queryRunner.query(sql, resultSet -> {
                List<String> cols1 = new ArrayList<>();
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i =1;i<=columnCount;i++){
                    String columnName = metaData.getColumnLabel(i);
                    if (null == columnName || 0 == columnName.length()) {
                        columnName = metaData.getColumnName(i);
                    }
                    cols1.add(columnName);
                }
                return cols1;
            });
            long end = System.currentTimeMillis();
            log(sql,null,cols,"RESULT-SIZE",(end-start));
        } catch (SQLException e) {
            throw new DBException(e);
        }finally {
            ExtraParamInjector.unsetSqlId();
        }
        return cols;
    }
    
    public Map<String, String> getTableMetas(){
        Map<String, String> map = new HashMap<>();
        DataSource dataSource = queryRunner.getDataSource();
        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet tablesRs = md.getTables(null, null, "%", null);
            while (tablesRs.next()) {
                String tableName = tablesRs.getString("TABLE_NAME");
                //make sure jdbc url contains parameter: useInformationSchema=true (MySQL)
                String comment = tablesRs.getString("REMARKS");
                map.put(tableName,comment);
            }
        }catch (Exception e){
            throw new DBException(e);
        }
        return map;
    }

    private void log(String sql,Object[] values,Object output,String outputDenote,long elapsed){
        try {
            Boolean ignoreLog = GeneralThreadLocal.get("ignoreLog");
            String log ="===> SQL: "+sql;
            if(values!=null){
                log += "  VALUES: "+new ArrayList<>(Arrays.asList(values));
            }
            if(output!=null) {
                if("RESULT-SIZE".equalsIgnoreCase(outputDenote)){
                    log += "\n<=== "+outputDenote+": " + ((List) output).size();
                }else {
                    log += "\n<=== "+outputDenote+": " + output;
                }
            }
            log +="\n<==> ELAPSED: "+elapsed+" ms.";
            if(ignoreLog==null||!ignoreLog){
                logger.debug(log);
            }
        } catch (Exception e) {
            logger.info(e.getMessage(),e);
        }
    }

    private String getIdSql(String sql){
        String idSql = sql;
        String sqlId = ExtraParamInjector.getSqlId();
        if(StringUtils.isNotBlank(sqlId)){
            idSql = "/* "+sqlId+" */ "+sql;
        }
        return idSql;
    }
}
