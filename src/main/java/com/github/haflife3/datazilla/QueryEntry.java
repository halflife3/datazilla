package com.github.haflife3.datazilla;

import com.github.haflife3.datazilla.annotation.TblField;
import com.github.haflife3.datazilla.dialect.DialectFactory;
import com.github.haflife3.datazilla.dialect.pagination.OfflinePagination;
import com.github.haflife3.datazilla.logic.SqlBuilder;
import com.github.haflife3.datazilla.logic.TableLoc;
import com.github.haflife3.datazilla.logic.TableObjectMetaCache;
import com.github.haflife3.datazilla.misc.DBException;
import com.github.haflife3.datazilla.misc.GeneralThreadLocal;
import com.github.haflife3.datazilla.misc.MiscUtil;
import com.github.haflife3.datazilla.misc.PagingInjector;
import com.github.haflife3.datazilla.pojo.*;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class QueryEntry {
    private final CoreRunner coreRunner;

    public QueryEntry(DataSource ds) {
        this.coreRunner = new CoreRunner(new QueryRunner(ds));
    }

    public QueryEntry(QueryRunner qr) {
        this.coreRunner = new CoreRunner(qr);
    }

    public QueryEntry(CoreRunner coreRunner) {
        this.coreRunner = coreRunner;
    }

    public CoreRunner getCoreRunner() {
        return coreRunner;
    }

    public DataSource getDataSource(){
        return coreRunner.getDataSource();
    }

    public String getDbType() {
        return coreRunner.getDbType();
    }

    public List<String> getColNames(Class<?> clazz){
        return getColNames(TableLoc.findTableName(clazz));
    }

    public List<String> getColNames(String table){
        return coreRunner.getColNames(table);
    }

    public Map<String, String> getTableMetas(){
        return coreRunner.getTableMetas();
    }

    public <T> List<T> genericQry(String sql, Class<T> clazz, Object ... values){
        return coreRunner.genericQry(sql,clazz,values);
    }

    public List<Map<String,Object>> genericQry(String sql, Object ... values){
        return coreRunner.genericQry(sql, values);
    }


    public <T> List<T> genericQry(String sql, ResultSetHandler<List<T>> resultSetHandler, Object ... values)  {
        return coreRunner.genericQry(sql,resultSetHandler,values);
    }

    public int genericUpdate(String sql, Object[] values){
        return coreRunner.genericUpdate(sql, values);
    }

    public String genericQry2Str(QueryConditionBundle qryCondition){
        String result = "";
        if(qryCondition.getResultClass()==null) {
            List<Map<String, Object>> listMap = coreRunner.genericMapQry(qryCondition);
            if (CollectionUtils.isNotEmpty(listMap)) {
                result = new Gson().toJson(listMap);
            }
        }else{
            List<Object> objects = coreRunner.genericQry(qryCondition);
            if(CollectionUtils.isNotEmpty(objects)){
                result = new Gson().toJson(objects);
            }
        }
        return result;
    }

    private Map<String,Object> fromFieldValuePair(List<FieldValuePair> pairs){
        Map<String,Object> map = new LinkedHashMap<>();
        if(CollectionUtils.isNotEmpty(pairs)){
            for(FieldValuePair pair:pairs){
                map.put(pair.getField(),pair.getValue());
            }
        }
        return map;
    }

    public int delObjects(Object obj){
        return delObjects(TableLoc.findTableName(obj.getClass()),fromTableDomain(obj));
    }

    public int delObjects(String table, Cond... conds){
        return delObjects(table,Arrays.asList(conds));
    }

    public int delObjects(String table, List<Cond> conds){
        ConditionBundle delCond = new ConditionBundle.Builder()
                .targetTable(table)
                .conditionAndList(conds)
                .build();
        SqlPreparedBundle sqlPreparedBundle = new SqlBuilder(coreRunner.getDbType()).composeDelete(delCond);
        return coreRunner.genericUpdate(sqlPreparedBundle.getSql(), sqlPreparedBundle.getValues());
    }

    public int delObjects(Class<?> clazz, Cond... conds){
        return delObjects(TableLoc.findTableName(clazz), Arrays.asList(conds));
    }
    public <T> List<T> findObjects(List<Cond> conds, Class<T> clazz){
        return findObjects(TableLoc.findTableName(clazz), conds, clazz);
    }
    public <T> List<T> findObjects(Class<T> clazz, Cond... conds){
        return findObjects(TableLoc.findTableName(clazz),Arrays.asList(conds),clazz);
    }

    public <T> List<T> findObjects(String table, List<Cond> conds, Class<T> clazz){
        List<T> rtList;
        try {
            PagingInjector.dropResult();
            QueryConditionBundle qryCondition = new QueryConditionBundle.Builder()
                    .resultClass(clazz)
                    .targetTable(table)
                    .conditionAndList(conds)
                    .pageNo(PagingInjector.getPageNo())
                    .pageSize(PagingInjector.getPageSize())
                    .orderByConds(PagingInjector.getOrderConds())
                    .build();
            rtList = genericQry(qryCondition);
            if(PagingInjector.isNeedCount()){
                QueryConditionBundle qcCount = new QueryConditionBundle.Builder()
                    .targetTable(qryCondition.getTargetTable())
                    .onlyCount(true)
                    .resultClass(CountInfo.class)
                    .conditionAndList(qryCondition.getConditionAndList())
                    .conditionOrList(qryCondition.getConditionOrList())
                    .build();
                List<CountInfo> counts = genericQry(qcCount);
                PagingInjector.setCount(counts.get(0).getCount());
            }
        } finally {
            PagingInjector.unSet();
        }
        return rtList;
    }

    public <T> List<T> searchObjects(T obj){
        List<Cond> conds = fromTableDomain(obj);
        Class<?> clazz = obj.getClass();
        return findObjects(conds, (Class<T>) clazz);
    }
    public <T> T searchObject(T obj){
        return MiscUtil.getFirst(searchObjects(obj));
    }

    public <T> T findObject(List<Cond> conds, Class<T> clazz){
        return findObject(TableLoc.findTableName(clazz), conds, clazz);
    }

    public <T> T findObject(Class<T> clazz, Cond... conds){
        return findObject(TableLoc.findTableName(clazz),Arrays.asList(conds),clazz);
    }

    public <T> T findObject(String table, List<Cond> conds, Class<T> clazz){
        return MiscUtil.getFirst(findObjects(table, conds, clazz));
    }

    public int insert(String table,List<FieldValuePair> pairs){
        return insert(table, fromFieldValuePair(pairs));
    }

    public int insert(String table,Map<String,Object> valueMap){
        stripUnknownFields(table,valueMap);
        return coreRunner.insert(table, valueMap);
    }

    public int insert(Object ... records){
        if(records!=null&&records.length>0){
            return insert(TableLoc.findTableName(records[0].getClass()), records);
        }
        return 0;
    }
    public <T> T insertAndReturnAutoGen(Object record){
        return coreRunner.insertWithReturn(TableLoc.findTableName(record.getClass()),toFieldValueMap(record));
    }
    public int insert(String table,Object ... records){
        int num = 0;
        if(records!=null) {
            for(Object record:records) {
                if(record!=null) {
                    Map<String, Object> valueMap = toFieldValueMap(record);
                    num += insert(table, valueMap);
                }
            }
        }
        return num;
    }

    public int batchInsert(Object ... records){
        return batchInsert(100,records);
    }

    public int batchInsert(int bulkSize,Object ... records){
        if(records!=null&&records.length>0){
            Object first = records[0];
            if(records.length==1&& first instanceof List){
                List<?> list = (List<?>) first;
                if(CollectionUtils.isNotEmpty(list)){
                    String tableName = TableLoc.findTableName(list.get(0).getClass());
                    return batchInsert(tableName,bulkSize, list.toArray());
                }
            }else {
                String tableName = TableLoc.findTableName(first.getClass());
                return batchInsert(tableName, bulkSize, records);
            }
        }
        return 0;
    }

    public int batchInsert(String table,int bulkSize,Object ... records){
        int num = 0;
        if(records!=null) {
            Map<String,List<Map<String, Object>>> dataMap = new HashMap<>();
            for(Object record:records) {
                if (record != null) {
                    Map<String, Object> valueMap = toFieldValueMap(record);
                    stripUnknownFields(table,valueMap);
                    List<String> keys = valueMap.keySet().stream().sorted().collect(Collectors.toList());
                    String keysStr = keys.toString();
                    if(dataMap.containsKey(keysStr)){
                        dataMap.get(keysStr).add(valueMap);
                    }else {
                        List<Map<String, Object>> listMap = new ArrayList<>();
                        listMap.add(valueMap);
                        dataMap.put(keysStr,listMap);
                    }
                }
            }
            for (List<Map<String, Object>> list : dataMap.values()) {
                List<List<Map<String, Object>>> partitions = Lists.partition(list, bulkSize);
                for (List<Map<String, Object>> partition : partitions) {
                    num+=coreRunner.batchInsert(table,partition);
                }
            }
        }
        return num;
    }

    public <T> int persistAutoCon(T record, T condObj){
        return persist(record,fromTableDomain(condObj));
    }

    public int persist(Object record, Cond... conds){
        return persist(record,Arrays.asList(conds));
    }

    public int persist(Object record, List<Cond> conds){
        int num = 0;
        num = updateSelective(record,conds);
        if(num == 0){
            num = insert(record);
        }
        return num;
    }

    public <T> int updateSelectiveAutoCon(T record, T condObj){
        return updateSelective(record,fromTableDomain(condObj));
    }

    public int updateSelective(Object record, List<Cond> conds){
        return updateSelective(TableLoc.findTableName(record.getClass()), record, conds);
    }

    public int updateSelective(Object record, Cond... conds){
        return updateSelective(TableLoc.findTableName(record.getClass()),record,Arrays.asList(conds));
    }

    public int updateSelective(Class<?> tableClass, Map<String, Object> updateValueMap, Cond... conds){
        return updateSelective(TableLoc.findTableName(tableClass),updateValueMap,Arrays.asList(conds));
    }

    public int updateSelective(String table, Object record, List<Cond> conds){
        return updateSelective(table,toFieldValueMap(record),conds);
    }

    public int updateSelective(String table, Map<String, Object> updateValueMap, List<Cond> conds){
        stripUnknownFields(table,updateValueMap);
        List<FieldValuePair> pairs = toFieldValuePair(updateValueMap);
        UpdateConditionBundle upCond = new UpdateConditionBundle.Builder()
            .targetTable(table)
            .values2Update(pairs)
            .conditionAndList(conds)
            .build();
        SqlPreparedBundle sqlPreparedBundle = new SqlBuilder(coreRunner.getDbType()).composeUpdate(upCond);
        return coreRunner.genericUpdate(sqlPreparedBundle.getSql(),sqlPreparedBundle.getValues());
    }

    public <T> int updateFull(T record, T condObj, List<String> excludeFields){
        return updateFull(record,fromTableDomain(condObj),excludeFields);
    }

    public int updateFull(Object record, List<Cond> conds, List<String> excludeFields){
        return updateFull(TableLoc.findTableName(record.getClass()),record, conds,excludeFields);
    }

    public int updateFull(String table, Object record, List<Cond> conds, List<String> excludeFields){
        Map<String, Object> map = toFullFieldValueMap(record);
        if(CollectionUtils.isNotEmpty(excludeFields)){
            excludeFields.forEach(map::remove);
        }
        return updateFull(table, map, conds);
    }
    public int updateFull(String table, Map<String, Object> valueMap, List<Cond> conds){
        stripUnknownFields(table,valueMap);
        List<FieldValuePair> pairs = toFullFieldValuePair(valueMap);
        UpdateConditionBundle upCond = new UpdateConditionBundle.Builder()
                .targetTable(table)
                .values2Update(pairs)
                .conditionAndList(conds)
                .build();
        SqlPreparedBundle sqlPreparedBundle = new SqlBuilder(coreRunner.getDbType()).composeUpdate(upCond);
        return coreRunner.genericUpdate(sqlPreparedBundle.getSql(),sqlPreparedBundle.getValues());
    }

    public <T> List<T> genericQry(QueryConditionBundle qryCondition){
        return coreRunner.genericQry(qryCondition);
    }
    public boolean exist(String table, List<Cond> conds){
        QueryConditionBundle qryCondition = new QueryConditionBundle.Builder()
                .resultClass(CountInfo.class)
                .onlyCount(true)
                .targetTable(table)
                .conditionAndList(conds)
                .build();
        List<CountInfo> countInfos = genericQry(qryCondition);
        return MiscUtil.getFirst(countInfos).getCount()>0;
    }
    public boolean exist(Class<?> clazz, Cond... conds){
        return exist(TableLoc.findTableName(clazz),Arrays.asList(conds));
    }

    public <T> boolean exist(T obj){
        return exist(TableLoc.findTableName(obj.getClass()),fromTableDomain(obj));
    }

    public int count(String table, List<Cond> conds){
        int count = 0;
        QueryConditionBundle qcCount = new QueryConditionBundle.Builder()
            .targetTable(table)
            .onlyCount(true)
            .resultClass(CountInfo.class)
            .conditionAndList(conds)
            .build();
        List<CountInfo> counts = genericQry(qcCount);
        count = counts.get(0).getCount();
        return count;
    }
    public int count(Class<?> clazz, Cond... conds){
        return count(TableLoc.findTableName(clazz),Arrays.asList(conds));
    }
    public <T> int count(T obj){
        return count(TableLoc.findTableName(obj.getClass()),fromTableDomain(obj));
    }

    public <T> PagedResult<T> commonPagedQuery(QueryConditionBundle qc){
        PagedResult<T> result = new PagedResult<>();
        List<T> records = genericQry(qc);
        Integer pageNo = qc.getPageNo();
        Integer pageSize = qc.getPageSize();
        if(pageNo!=null&&pageSize!=null&&pageNo!=0&&pageSize!=0) {
            OfflinePagination offlinePagination = DialectFactory.getOfflinePagination(coreRunner.getDbType());
            if(offlinePagination!=null){
                records = offlinePagination.paginate(records,pageNo,pageSize);
                if(records!=null){
                    result.setTotalCount(records.size());
                }else {
                    result.setTotalCount(0);
                }
            }else {
                QueryConditionBundle qcCount = new QueryConditionBundle.Builder()
                    .targetTable(qc.getTargetTable())
                    .onlyCount(true)
                    .resultClass(CountInfo.class)
                    .conditionAndList(qc.getConditionAndList())
                    .conditionOrList(qc.getConditionOrList())
                    .build();
                List<CountInfo> counts = genericQry(qcCount);
                result.setTotalCount(counts.get(0).getCount());
            }
        }else{
            result.setTotalCount(records.size());
        }
        result.setResult(records);
        return result;
    }

    private List<FieldValuePair> toFieldValuePair(Map<String, Object> map){
        List<FieldValuePair> pairs = toFullFieldValuePair(map);
        pairs.removeIf(pair -> pair.getValue()==null);
        return pairs;
    }

    private List<FieldValuePair> toFullFieldValuePair(Map<String, Object> map){
        List<FieldValuePair> pairs = new ArrayList<>();
        if(MapUtils.isNotEmpty(map)){
            map.forEach((key,value) ->{
                pairs.add(new FieldValuePair(key,value));
            });
        }
        return pairs;
    }

    private  List<Cond> fromTableDomain(Object obj){
        List<Cond> conds = new ArrayList<>();
        Map<String,Object> condMap = toFieldValueMap(obj);
        condMap.forEach((fieldName,value)-> conds.add(new Cond(fieldName,value)));
        return conds;
    }

    private Map<String,Object> toFieldValueMap(Object obj) {
        Map<String, Object> fieldValueMap = toFullFieldValueMap(obj);
        fieldValueMap.entrySet().removeIf(entry->entry.getValue()==null);
        return fieldValueMap;
    }

    private Map<String,Object> toFullFieldValueMap(Object obj) {
        Map<String,Object> condMap = new LinkedHashMap<>();
        try {
            Class<?> tableClass = obj.getClass();
            TableObjectMetaCache.initTableObjectMeta(tableClass,this);
            boolean metaInitComplete = TableObjectMetaCache.metaInitComplete(tableClass);
            List<Field> fields = MiscUtil.getAllFields(tableClass);
            if(!metaInitComplete) {
                for (Field field : fields) {
                    if (field.isSynthetic()) {
                        continue;
                    }
                    if (field.isAnnotationPresent(TblField.class)) {
                        field.setAccessible(true);
                        TblField tblField = field.getAnnotation(TblField.class);
                        String fieldName = StringUtils.isNotBlank(tblField.customField()) ? tblField.customField() : tblField.value();
                        if (StringUtils.isBlank(fieldName)) {
                            fieldName = field.getName();
                        }
                        Object value = field.get(obj);
                        condMap.put(fieldName, value);
                    }
                }
            }else {
                Map<String, String> fieldToColumnMap = TableObjectMetaCache.getFieldToColumnMap(tableClass);
                for (Field field : fields) {
                    if (!field.isSynthetic()) {
                        String fieldName = field.getName();
                        if(!fieldToColumnMap.containsKey(fieldName)){
                            continue;
                        }
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        condMap.put(fieldToColumnMap.get(fieldName), value);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new DBException(e);
        }
        return condMap;
    }

    private void stripUnknownFields(String table, Map<String, Object> fieldMap){
        Boolean needStrip = GeneralThreadLocal.get("stripUnknownFields");
        if(needStrip==null||!needStrip){
            return;
        }
        String key = this.toString()+"_"+table;
        List<String> validCols = new ArrayList<>();
        if(GeneralThreadLocal.containsKey(key)){
            validCols = GeneralThreadLocal.get(key);
        }else {
            List<String> origValidCols = getColNames(table);
            for (String origValidCol : origValidCols) {
                validCols.add(origValidCol.toLowerCase());
            }
            GeneralThreadLocal.set(key,validCols);
        }
        List<String> finalValidCols = validCols;
        fieldMap.entrySet().removeIf(entry->!finalValidCols.contains(entry.getKey().toLowerCase()));
    }
    public List<Cond> buildConds(Object obj){
        return new SqlBuilder(coreRunner.getDbType()).buildConds(obj);
    }
}
