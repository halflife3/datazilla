package com.github.haflife3.datazilla;

import com.github.haflife3.datazilla.annotation.Primary;
import com.github.haflife3.datazilla.logic.SqlBuilder;
import com.github.haflife3.datazilla.logic.TableLoc;
import com.github.haflife3.datazilla.logic.TableObjectMetaCache;
import com.github.haflife3.datazilla.misc.DBException;
import com.github.haflife3.datazilla.misc.ExtraParamInjector;
import com.github.haflife3.datazilla.misc.MiscUtil;
import com.github.haflife3.datazilla.misc.PagingInjector;
import com.github.haflife3.datazilla.pojo.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryEntry {
    private final CoreRunner coreRunner;

    public QueryEntry(DataSource ds) {
        this.coreRunner = new CoreRunner(new QueryRunner(ds));
    }

    public QueryEntry(DataSource ds, String dbType) {
        this.coreRunner = new CoreRunner(new QueryRunner(ds), dbType);
    }

    public QueryEntry(QueryRunner qr) {
        this.coreRunner = new CoreRunner(qr);
    }

    public QueryEntry(QueryRunner qr, String dbType) {
        this.coreRunner = new CoreRunner(qr, dbType);
    }

    public QueryEntry(CoreRunner coreRunner) {
        this.coreRunner = coreRunner;
    }

    public CoreRunner getCoreRunner() {
        return coreRunner;
    }

    public QueryRunner getQueryRunner() {
        return coreRunner.getQueryRunner();
    }

    public DataSource getDataSource() {
        return coreRunner.getDataSource();
    }

    public String getDbType() {
        return coreRunner.getDbType();
    }

    public List<String> getColNames(Class<?> clazz) {
        return getColNames(TableLoc.findTableName(clazz));
    }

    public List<String> getColNames(String table) {
        return coreRunner.getColNames(table);
    }

    public Map<String, String> getTableMetas() {
        return coreRunner.getTableMetas();
    }

    public <T> List<T> genericQry(String sql, Class<T> clazz, Object... values) {
        return coreRunner.genericQry(sql, clazz, values);
    }

    public List<Map<String, Object>> genericQry(String sql, Object... values) {
        return coreRunner.genericQry(sql, values);
    }


    public <T> T genericQry(String sql, ResultSetHandler<T> resultSetHandler, Object... values) {
        return coreRunner.genericQry(sql, resultSetHandler, values);
    }

    public int genericUpdate(String sql, Object... values) {
        return coreRunner.genericUpdate(sql, values);
    }

    public int delObjects(String table, List<Cond> conds) {
        try {
            ConditionBundle delCond = new ConditionBundle.Builder()
                    .targetTable(table)
                    .conditionAndList(combineConds(conds, ExtraParamInjector.getExtraConds()))
                    .build();
            boolean noCond = CollectionUtils.isEmpty(delCond.getConditionAndList());
            if(noCond&&!ExtraParamInjector.emptyUpdateCondAllowed()){
                throw new DBException("Delete without condition! This restriction can be suppressed by ExtraParamInjector.allowEmptyUpdateCond()");
            }
            SqlPreparedBundle sqlPreparedBundle = new SqlBuilder(coreRunner).composeDelete(delCond);
            return coreRunner.genericUpdate(sqlPreparedBundle.getSql(), sqlPreparedBundle.getValues());
        } finally {
            ExtraParamInjector.unsetExtraConds();
            ExtraParamInjector.unsetEmptyUpdateCondRestriction();
        }
    }

    public <E> int delObjects(Class<?> clazz, CondCrafter<E> condCrafter, E primalCond) {
        return delObjects(TableLoc.findTableName(clazz), condCrafter.craft(primalCond));
    }

    public int delObjects(Object obj) {
        return delObjects(obj.getClass(), this::fromTableDomain, obj);
    }

    public int delObjects(String table, Cond... conds) {
        return delObjects(table, Arrays.asList(conds));
    }

    public int delObjects(Class<?> clazz, final Cond... conds) {
        return delObjects(clazz, Arrays::asList, conds);
    }

    public <T> List<T> genericQry(QueryConditionBundle qryCondition) {
        return coreRunner.genericQry(qryCondition);
    }

    public <T> List<T> findObjects(String table, List<Cond> conds, Class<T> clazz) {
        List<T> rtList;
        try {
            String sqlId = ExtraParamInjector.getSqlId();
            PagingInjector.dropResult();
            QueryConditionBundle qryCondition = new QueryConditionBundle.Builder()
                    .resultClass(clazz)
                    .targetTable(table)
                    .conditionAndList(combineConds(conds, ExtraParamInjector.getExtraConds()))
                    .conditionOrList(ExtraParamInjector.getExtraOrConds())
                    .selectColumns(ExtraParamInjector.getSelectColumns())
                    .offset(PagingInjector.getOffset())
                    .limit(PagingInjector.getLimit())
                    .orderByConds(PagingInjector.getOrderConds())
                    .build();
            rtList = genericQry(qryCondition);
            rtList = coreRunner.getOfflinePagination().paginate(rtList, qryCondition.getOffset(), qryCondition.getLimit());
            if (PagingInjector.needCount()) {
                QueryConditionBundle qcCount = new QueryConditionBundle.Builder()
                        .targetTable(qryCondition.getTargetTable())
                        .onlyCount(true)
                        .resultClass(CountInfo.class)
                        .conditionAndList(qryCondition.getConditionAndList())
                        .conditionOrList(qryCondition.getConditionOrList())
                        .build();
                ExtraParamInjector.sqlId(sqlId);
                List<CountInfo> counts = genericQry(qcCount);
                PagingInjector.setCount(counts.get(0).getCount());
            }
        } finally {
            ExtraParamInjector.unSet();
        }
        return rtList;
    }

    public <T, E> List<T> findObjects(Class<T> clazz, CondCrafter<E> condCrafter, E primalCond) {
        return findObjects(TableLoc.findTableName(clazz), condCrafter.craft(primalCond), clazz);
    }

    public <T> List<T> findObjects(List<Cond> conds, Class<T> clazz) {
        return findObjects(clazz, conds);
    }

    public <T> List<T> findObjects(Class<T> clazz, List<Cond> conds) {
        return findObjects(clazz, (primalCond) -> primalCond, conds);
    }

    public <T> List<T> findObjects(Class<T> clazz, Cond... conds) {
        return findObjects(clazz, Arrays::asList, conds);
    }

    public <T> T findObject(List<Cond> conds, Class<T> clazz) {
        return findObject(clazz, conds);
    }

    public <T> T findObject(Class<T> clazz, List<Cond> conds) {
        return MiscUtil.getFirst(findObjects(clazz, (primalCond) -> primalCond, conds));
    }

    public <T> T findObject(String table, List<Cond> conds, Class<T> clazz) {
        return MiscUtil.getFirst(findObjects(table, conds, clazz));
    }

    public <T> T findObject(Class<T> clazz, Cond... conds) {
        return findObject(TableLoc.findTableName(clazz), Arrays.asList(conds), clazz);
    }

    public <T> List<T> searchObjects(T obj) {
        List<Cond> conds = fromTableDomain(obj);
        Class<?> clazz = obj.getClass();
        return findObjects((Class<T>) clazz, conds);
    }

    public <T> T searchObject(T obj) {
        return MiscUtil.getFirst(searchObjects(obj));
    }

    public int insert(String table, Map<String, Object> valueMap) {
        return coreRunner.insert(table, valueMap);
    }

    public int insert(String table, Object... records) {
        int num = 0;
        if (records != null) {
            for (Object record : records) {
                if (record != null) {
                    Map<String, Object> valueMap = toFieldValueMap(record);
                    num += insert(table, valueMap);
                }
            }
        }
        return num;
    }

    public int insert(Object... records) {
        int num = 0;
        if (records != null && records.length > 0) {
            for (Object record : records) {
                num += insert(TableLoc.findTableName(record.getClass()), record);
            }
        }
        return num;
    }

    public <T> T insertAndReturnAutoGen(Object record) {
        return coreRunner.insertWithReturn(TableLoc.findTableName(record.getClass()), toFieldValueMap(record));
    }

    public int batchInsert(String table, int bulkSize, Object... records) {
        int num = 0;
        if (records != null) {
            Map<String, List<Map<String, Object>>> dataMap = new HashMap<>();
            for (Object record : records) {
                if (record != null) {
                    Map<String, Object> valueMap = toFieldValueMap(record);
                    List<String> keys = valueMap.keySet().stream().sorted().collect(Collectors.toList());
                    String keysStr = keys.toString();
                    if (dataMap.containsKey(keysStr)) {
                        dataMap.get(keysStr).add(valueMap);
                    } else {
                        List<Map<String, Object>> listMap = new ArrayList<>();
                        listMap.add(valueMap);
                        dataMap.put(keysStr, listMap);
                    }
                }
            }
            String sqlId = ExtraParamInjector.getSqlId();
            for (List<Map<String, Object>> list : dataMap.values()) {
                List<List<Map<String, Object>>> partitions = Lists.partition(list, bulkSize);
                for (List<Map<String, Object>> partition : partitions) {
                    ExtraParamInjector.sqlId(sqlId);
                    num += coreRunner.batchInsert(table, partition);
                }
            }
        }
        return num;
    }

    public int batchInsert(int bulkSize, Object... records) {
        if (records != null && records.length > 0) {
            Object first = records[0];
            if (records.length == 1 && first instanceof List) {
                List<?> list = (List<?>) first;
                if (CollectionUtils.isNotEmpty(list)) {
                    String tableName = TableLoc.findTableName(list.get(0).getClass());
                    return batchInsert(tableName, bulkSize, list.toArray());
                }
            } else {
                String tableName = TableLoc.findTableName(first.getClass());
                return batchInsert(tableName, bulkSize, records);
            }
        }
        return 0;
    }

    public int batchInsert(Object... records) {
        return batchInsert(100, records);
    }

    public int update(String table, Map<String, Object> updateValueMap, List<Cond> conds) {
        try {
            List<FieldValuePair> pairs = toFullFieldValuePair(updateValueMap);
            UpdateConditionBundle upCond = new UpdateConditionBundle.Builder()
                    .targetTable(table)
                    .values2Update(pairs)
                    .conditionAndList(combineConds(conds, ExtraParamInjector.getExtraConds()))
                    .conditionOrList(ExtraParamInjector.getExtraOrConds())
                    .build();
            boolean noCond = CollectionUtils.isEmpty(upCond.getConditionAndList()) && CollectionUtils.isEmpty(upCond.getConditionOrList());
            if(noCond&&!ExtraParamInjector.emptyUpdateCondAllowed()){
                throw new DBException("Update without condition! This restriction can be suppressed by ExtraParamInjector.allowEmptyUpdateCond()");
            }
            SqlPreparedBundle sqlPreparedBundle = new SqlBuilder(coreRunner).composeUpdate(upCond);
            return coreRunner.genericUpdate(sqlPreparedBundle.getSql(), sqlPreparedBundle.getValues());
        } finally {
            ExtraParamInjector.unsetExtraConds();
            ExtraParamInjector.unsetExtraOrConds();
            ExtraParamInjector.unsetEmptyUpdateCondRestriction();
        }
    }

    public int updateSelective(String table, Object record, List<Cond> conds) {
        return update(table, toFieldValueMap(record), conds);
    }

    public int updateSelective(Object record, List<Cond> conds) {
        return updateSelective(TableLoc.findTableName(record.getClass()), record, conds);
    }

    public <E> int updateSelective(Object record, CondCrafter<E> condCrafter, E primalCond) {
        return updateSelective(record, condCrafter.craft(primalCond));
    }

    public int updateSelective(Object record, Cond... conds) {
        return updateSelective(record, Arrays::asList, conds);
    }

    public int updateSelectiveConcise(Object record, String... fieldsOrColumns) {
        return updateSelective(record, initCondsByFields(record, fieldsOrColumns));
    }

    public <T> int updateSelectiveAutoCond(T record, T condObj) {
        return updateSelective(record, this::fromTableDomain, condObj);
    }

    public int updateSelectiveByPrimary(Object record){
        return update(TableLoc.findTableName(record.getClass()),toFieldValueMap(record,false),getPrimaryConds(record));
    }

    public int persist(Object record, List<Cond> conds) {
        if (CollectionUtils.isEmpty(conds)) {
            throw new DBException("conditions can't be empty for persist");
        }
        String sqlId = ExtraParamInjector.getSqlId();
        int num;
        num = updateSelective(record, conds);
        if (num == 0) {
            ExtraParamInjector.sqlId(sqlId);
            num = insert(record);
        }
        return num;
    }

    public <E> int persist(Object record, CondCrafter<E> condCrafter, E primalCond) {
        return persist(record, condCrafter.craft(primalCond));
    }

    public int persist(Object record, Cond... conds) {
        return persist(record, Arrays::asList, conds);
    }

    public <T> int persistAutoCond(T record, T condObj) {
        return persist(record, this::fromTableDomain, condObj);
    }

    public int updateFull(String table, Object record, List<Cond> conds, List<String> excludeColumns, boolean includePrimary) {
        Map<String, Object> map = toFullFieldValueMap(record,includePrimary);
        if (CollectionUtils.isNotEmpty(excludeColumns)) {
            List<String> lowercaseColNames = excludeColumns.stream().map(String::toLowerCase).collect(Collectors.toList());
            List<String> keys2Remove = new ArrayList<>();
            map.forEach((key, value) -> {
                if (lowercaseColNames.contains(key.toLowerCase())) {
                    keys2Remove.add(key);
                }
            });
            keys2Remove.forEach(map::remove);
        }
        return update(table, map, conds);
    }

    public int updateFull(Object record, List<Cond> conds, String ... excludeColumns) {
        return updateFull(TableLoc.findTableName(record.getClass()), record, conds, Arrays.asList(excludeColumns),true);
    }

    public <E> int updateFull(Object record, CondCrafter<E> condCrafter, E primalCond, String ... excludeColumns) {
        return updateFull(record, condCrafter.craft(primalCond), excludeColumns);
    }

    public <T> int updateFull(T record, T condObj, String ... excludeColumns) {
        return updateFull(record, this::fromTableDomain, condObj, excludeColumns);
    }

    public int updateFullByPrimary(Object record, String ... excludeColumns){
        return updateFull(TableLoc.findTableName(record.getClass()),record,getPrimaryConds(record),Arrays.asList(excludeColumns),false);
    }

    public boolean exist(Class<?> clazz, List<Cond> conds) {
        return findObject(conds, clazz) != null;
    }

    public <E> boolean exist(Class<?> clazz, CondCrafter<E> condCrafter, E primalCond) {
        return exist(clazz, condCrafter.craft(primalCond));
    }

    public boolean exist(Class<?> clazz, Cond... conds) {
        return exist(clazz, Arrays::asList, conds);
    }

    public <T> boolean exist(T obj) {
        if(obj instanceof Class){
            return exist((Class<?>) obj,new ArrayList<>());
        }
        return exist(obj.getClass(), this::fromTableDomain, obj);
    }

    public int count(Class<?> clazz, List<Cond> conds) {
        int count = 0;
        try {
            QueryConditionBundle qcCount = new QueryConditionBundle.Builder()
                    .targetTable(TableLoc.findTableName(clazz))
                    .onlyCount(true)
                    .resultClass(CountInfo.class)
                    .conditionAndList(combineConds(conds, ExtraParamInjector.getExtraConds()))
                    .conditionOrList(ExtraParamInjector.getExtraOrConds())
                    .build();
            List<CountInfo> counts = genericQry(qcCount);
            count = counts.get(0).getCount();
        } finally {
            ExtraParamInjector.unsetExtraConds();
            ExtraParamInjector.unsetExtraOrConds();
        }
        return count;
    }

    public <E> int count(Class<?> clazz, CondCrafter<E> condCrafter, E primalCond) {
        return count(clazz, condCrafter.craft(primalCond));
    }

    public int count(Class<?> clazz, Cond... conds) {
        return count(clazz, Arrays::asList, conds);
    }

    public <T> int count(T obj) {
        if(obj instanceof Class){
            return count((Class<?>) obj,new ArrayList<>());
        }
        return count(obj.getClass(), this::fromTableDomain, obj);
    }

    public List<Cond> fromTableDomain(Object obj) {
        List<Cond> conds = new ArrayList<>();
        Map<String, Object> condMap = toFieldValueMap(obj);
        condMap.forEach((fieldName, value) -> conds.add(new Cond(fieldName, value)));
        return conds;
    }

    public List<Cond> buildConds(Object obj) {
        return new SqlBuilder(coreRunner).buildConds(obj);
    }

    private List<Cond> initCondsByFields(Object obj, String[] fieldOrColumnArr) {
        List<Cond> conds = new ArrayList<>();
        try {
            if (fieldOrColumnArr == null || fieldOrColumnArr.length == 0) {
                return conds;
            }
            Class<?> tableClass = obj.getClass();
            TableObjectMetaCache.initTableObjectMeta(tableClass, this);
            Map<String, String> fieldToColumnMap = TableObjectMetaCache.getFieldToColumnMap(tableClass);
            Map<String, String> columnToFieldMap = TableObjectMetaCache.getColumnToFieldMap(tableClass);
            Map<String, Field> fieldMap = MiscUtil.mapFieldFromObj(obj);
            for (String fieldOrColumn : fieldOrColumnArr) {
                boolean match = false;
                String colName = null;
                Field field = null;
                if (fieldToColumnMap.containsKey(fieldOrColumn)) {
                    field = fieldMap.get(fieldOrColumn);
                    colName = fieldToColumnMap.get(fieldOrColumn);
                    match = true;
                } else if (columnToFieldMap.containsKey(fieldOrColumn.toLowerCase())) {
                    String fieldName = columnToFieldMap.get(fieldOrColumn);
                    field = fieldMap.get(fieldName);
                    colName = fieldOrColumn;
                    match = true;
                }
                if (!match) {
                    throw new DBException("fieldOrColumn:" + fieldOrColumn + " can't be recognized!");
                }
                field.setAccessible(true);
                conds.add(new Cond(colName, field.get(obj)));
            }
        } catch (IllegalAccessException e) {
            throw new DBException(e);
        }
        return conds;
    }

    private static List<Cond> combineConds(List<Cond> conds1, List<Cond> conds2) {
        return Stream.of(conds1, conds2)
                .filter(CollectionUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<FieldValuePair> toFullFieldValuePair(Map<String, Object> map) {
        List<FieldValuePair> pairs = new ArrayList<>();
        if (MapUtils.isNotEmpty(map)) {
            map.forEach((key, value) -> {
                pairs.add(new FieldValuePair(key, value));
            });
        }
        return pairs;
    }

    private Map<String, Object> toFieldValueMap(Object obj) {
        return toFieldValueMap(obj,true);
    }

    private Map<String, Object> toFieldValueMap(Object obj, boolean includePrimary) {
        Map<String, Object> fieldValueMap = toFullFieldValueMap(obj,includePrimary);
        fieldValueMap.entrySet().removeIf(entry -> entry.getValue() == null);
        return fieldValueMap;
    }

    private Map<String, Object> toFullFieldValueMap(Object obj) {
        return toFullFieldValueMap(obj,true);
    }

    private Map<String, Object> toFullFieldValueMap(Object obj, boolean includePrimary) {
        Map<String, Object> condMap = new LinkedHashMap<>();
        try {
            Class<?> tableClass = obj.getClass();
            TableObjectMetaCache.initTableObjectMeta(tableClass, this);
            List<Field> fields = MiscUtil.getAllFields(tableClass);
            Map<String, String> fieldToColumnMap = TableObjectMetaCache.getFieldToColumnMap(tableClass);
            for (Field field : fields) {
                String fieldName = field.getName();
                if (field.isSynthetic()||!fieldToColumnMap.containsKey(fieldName)) {
                    continue;
                }
                if(!includePrimary){
                    boolean isPrimary = field.isAnnotationPresent(Primary.class);
                    if (isPrimary){
                        continue;
                    }
                }
                field.setAccessible(true);
                Object value = field.get(obj);
                condMap.put(fieldToColumnMap.get(fieldName), value);
            }
        } catch (Exception e) {
            throw new DBException(e);
        }
        return condMap;
    }

    private List<Cond> getPrimaryConds(Object obj){
        List<Cond> conds = new ArrayList<>();
        try {
            Class<?> tableClass = obj.getClass();
            TableObjectMetaCache.initTableObjectMeta(tableClass, this);
            List<String> primaryFields = TableObjectMetaCache.getPrimaryFields(tableClass);
            if(CollectionUtils.isEmpty(primaryFields)){
                return conds;
            }
            Map<String, String> fieldToColumnMap = TableObjectMetaCache.getFieldToColumnMap(tableClass);
            List<Field> fields = MiscUtil.getAllFields(tableClass);
            for (Field field : fields) {
                String fieldName = field.getName();
                boolean isPrimary = field.isAnnotationPresent(Primary.class);
                if (field.isSynthetic()||!isPrimary||!primaryFields.contains(fieldName)||!fieldToColumnMap.containsKey(fieldName)) {
                    continue;
                }
                field.setAccessible(true);
                Object value = field.get(obj);
                conds.add(new Cond(fieldToColumnMap.get(fieldName),value));
            }
        } catch (Exception e) {
            throw new DBException(e);
        }

        return conds;
    }
}
