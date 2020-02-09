package com.github.haflife3.datazilla.logic;

import com.github.haflife3.datazilla.CoreRunner;
import com.github.haflife3.datazilla.QueryEntry;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.TblField;
import com.github.haflife3.datazilla.misc.MiscUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableObjectMetaCache {
    private final static Map<String, Map<String,String>> fieldToColumnClassMap = new ConcurrentHashMap<>();
    private final static Map<String, Map<String,String>> columnToFieldClassMap = new ConcurrentHashMap<>();

    public static void initTableObjectMeta(Class<?> tableClass, QueryEntry queryEntry){
        initTableObjectMeta(tableClass,queryEntry.getCoreRunner());
    }
    public static void initTableObjectMeta(Class<?> tableClass, CoreRunner coreRunner){
        Table table = tableClass.getAnnotation(Table.class);
        if(table==null||!table.autoColumnDetection()||metaInitComplete(tableClass)){
            return;
        }
        Map<String,String> fieldToColumnMap = new HashMap<>();
        Map<String,String> columnToFieldMap = new HashMap<>();
        List<String> colNames = coreRunner.getColNames(table.value());
        List<Field> fields = MiscUtil.getAllFields(tableClass);
        for (Field field : fields) {
            TblField tblField = field.getAnnotation(TblField.class);
            String fieldName = field.getName();
            String regulatedFieldName = fieldName.replace("_", "").toLowerCase();
            if(tblField!=null){
                String colName4SqlCompose = StringUtils.isNotBlank(tblField.customField())?tblField.customField():
                        (StringUtils.isNotBlank(tblField.value())?tblField.value():fieldName);
                String colName4FieldMapping = StringUtils.isNotBlank(tblField.value())?tblField.value():fieldName;
                fieldToColumnMap.put(fieldName,colName4SqlCompose);
                columnToFieldMap.put(colName4FieldMapping.toLowerCase(),fieldName);
            }else {
                colNames.forEach(colName -> {
                    String regulatedColName = colName.replace("_","").toLowerCase();
                    if(regulatedFieldName.equals(regulatedColName)){
                        fieldToColumnMap.put(fieldName,colName);
                        columnToFieldMap.put(colName.toLowerCase(),fieldName);
                    }
                });
            }
        }
        if(MapUtils.isNotEmpty(fieldToColumnMap)){
            fieldToColumnClassMap.put(tableClass.getName(),fieldToColumnMap);
        }
        if(MapUtils.isNotEmpty(columnToFieldMap)){
            columnToFieldClassMap.put(tableClass.getName(),columnToFieldMap);
        }
    }

    public static boolean metaInitComplete(Class<?> tableClass){
        return fieldToColumnClassMap.containsKey(tableClass.getName()) && columnToFieldClassMap.containsKey(tableClass.getName());
    }

    public static Map<String,String> getFieldToColumnMap(Class<?> tableClass){
        return fieldToColumnClassMap.get(tableClass.getName());
    }

    public static Map<String,String> getColumnToFieldMap(Class<?> tableClass){
        return columnToFieldClassMap.get(tableClass.getName());
    }
}
