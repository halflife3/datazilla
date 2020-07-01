package com.github.haflife3.datazilla;

import com.github.haflife3.datazilla.dialect.DialectConst;
import com.github.haflife3.datazilla.dialect.DialectFactory;
import com.github.haflife3.datazilla.dialect.regulate.EntityRegulator;
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
    private final EntityRegulator entityRegulator;

    public String getDbType() {
        return dbType;
    }

    public CoreRunner(QueryRunner queryRunner) {
        this.queryRunner = queryRunner;
        dbType = new PlatformUtils().determineDatabaseType(queryRunner.getDataSource());
        if(DialectFactory.SUPPORTED_DB.stream().noneMatch(dbType::equalsIgnoreCase)){
            dbType = DialectConst.DEFAULT;
        }
        sqlBuilder = new SqlBuilder(dbType);
        entityRegulator = DialectFactory.getEntityRegulator(dbType);
    }

    public DataSource getDataSource(){
        return queryRunner.getDataSource();
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
        table = entityRegulator.regulateTable(table);
        String sql = "insert into "+table+" (";
        String valueSql = " values( ";
        List<Object> values = new ArrayList<>();
        for(Map.Entry<String, Object> entry:valueMap.entrySet()){
            String field = entityRegulator.regulateField(entry.getKey());
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
            table = entityRegulator.regulateTable(table);
            String sql = "insert into "+table+" (";
            String valueSql = " values( ";
            List<Object> values = new ArrayList<>();
            for(Map.Entry<String, Object> entry:valueMap.entrySet()){
                String field = entityRegulator.regulateField(entry.getKey());
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
        return DialectFactory.getBatchInserter(dbType).batchInsert(this,table,listMap);
    }

    
    public List<String> getColNames(String table){
        List<String> cols = null;
        try {
            long start = System.currentTimeMillis();
            table = entityRegulator.regulateTable(table);
            String sql = "select * from "+table+" where 1=2";
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
                String comment = tablesRs.getString("REMARKS");//make sure jdbc url contains parameter: useInformationSchema=true
                map.put(tableName,comment);
            }
        }catch (Exception e){
            throw new DBException(e);
        }
        return map;
    }

    private void log(String sql,Object[] values,Object output,String outputDenote,long elapsed){
        try {
//            StackTraceElement[] stack = new Throwable().getStackTrace();
            Boolean ignoreLog = GeneralThreadLocal.get("ignoreDaoLog");
//            String log = conciseStack(stack)+"\n";
//            if(ignoreLog==null||!ignoreLog) {
//                logger.debug(conciseStack(stack));
//            }
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

    private String conciseStack(StackTraceElement[] stack){
        StringBuilder conciseInfo = new StringBuilder();
        if(stack!=null){
            List<String> ignoredNames = Arrays.asList("CoreRunner.java","QueryEntry.java","<generated>");
            List<StackTraceElement> elements = new ArrayList<>(Arrays.asList(stack));
            for(StackTraceElement element:elements){
                String className = element.getClassName();
                String fileName = element.getFileName();
                int lineNumber = element.getLineNumber();
                if(!ignoredNames.contains(fileName)){
                    String infoPart =  (fileName != null && lineNumber >= 0 ?
                            "(" + fileName + ":" + lineNumber + ")" :
                            (fileName != null ?  "("+fileName+")" : "(Unknown Source)"));
                    conciseInfo.append(infoPart).append(" <--");
                }
            }
        }
        String info = conciseInfo.toString();
        info = StringUtils.stripEnd(info," <--");
        return info;
    }
}
