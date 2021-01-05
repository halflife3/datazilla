package com.github.haflife3.datazilla.logic;

import com.github.haflife3.datazilla.CoreRunner;
import com.github.haflife3.datazilla.QueryEntry;
import com.github.haflife3.datazilla.annotation.Column;
import com.github.haflife3.datazilla.annotation.Primary;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.misc.DBException;
import com.github.haflife3.datazilla.misc.MiscUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableObjectMetaCache {
    private final static Map<String, Map<String,String>> fieldToColumnClassMap = new ConcurrentHashMap<>();
    private final static Map<String, Map<String,String>> columnToFieldClassMap = new ConcurrentHashMap<>();
    private final static Map<String, List<String>> primaryFieldsClassMap = new ConcurrentHashMap<>();

    public static void initTableObjectMeta(Class<?> tableClass, QueryEntry queryEntry){
        initTableObjectMeta(tableClass,queryEntry.getCoreRunner());
    }
    public static void initTableObjectMeta(Class<?> tableClass, CoreRunner coreRunner){
        String className = tableClass.getName();
        Table table = tableClass.getAnnotation(Table.class);
        if(table==null){
            throw new DBException("Init Table Meta failed: tableClass:"+ className +" has no @Table annotation!");
        }
        if(metaInitComplete(tableClass)){
            return;
        }
        boolean autoColumnDetection = table.autoColumnDetection();
        Map<String,String> fieldToColumnMap = new HashMap<>();
        Map<String,String> columnToFieldMap = new HashMap<>();
        List<String> primaryFields = new ArrayList<>();
        List<String> colNames = coreRunner.getColNames(table.value());
        List<Field> fields = MiscUtil.getAllFields(tableClass);
        for (Field field : fields) {
            // parsing Column info
            Column column = field.getAnnotation(Column.class);
            String fieldName = field.getName();
            String regulatedFieldName = fieldName.replace("_", "").toLowerCase();
            if(column!=null){
                String colName4SqlCompose = StringUtils.isNotBlank(column.customValue())?column.customValue():
                        (StringUtils.isNotBlank(column.value())?column.value():fieldName);
                String colName4FieldMapping = StringUtils.isNotBlank(column.value())?column.value():fieldName;
                fieldToColumnMap.put(fieldName,colName4SqlCompose);
                columnToFieldMap.put(colName4FieldMapping.toLowerCase(),fieldName);
            }else {
                if(autoColumnDetection){
                    colNames.forEach(colName -> {
                        String regulatedColName = colName.replace("_","").toLowerCase();
                        if(regulatedFieldName.equals(regulatedColName)){
                            fieldToColumnMap.put(fieldName,colName);
                            columnToFieldMap.put(colName.toLowerCase(),fieldName);
                        }
                    });
                }
            }
            // parsing Primary info
            Primary primary = field.getAnnotation(Primary.class);
            if(primary!=null){
                primaryFields.add(fieldName);
            }

        }
        primaryFieldsClassMap.put(className,primaryFields);
        if(MapUtils.isNotEmpty(fieldToColumnMap)){
            fieldToColumnClassMap.put(className,fieldToColumnMap);
        }
        if(MapUtils.isNotEmpty(columnToFieldMap)){
            columnToFieldClassMap.put(className,columnToFieldMap);
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

    public static List<String> getPrimaryFields(Class<?> tableClass) {
        return primaryFieldsClassMap.get(tableClass.getName());
    }
}
