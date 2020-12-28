package com.github.haflife3.datazilla.misc;

import com.github.haflife3.datazilla.annotation.Column;
import com.github.haflife3.datazilla.logic.TableObjectMetaCache;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MoreGenerousBeanProcessor extends CustomBeanProcessor {
    private final Class<?> beanClass;
    private final Map<String,Field> fieldMap;

    /**
     * Default constructor.
     */
    public MoreGenerousBeanProcessor(Class<?> beanClass) {
        super();
        this.beanClass = beanClass;
        this.fieldMap = MiscUtil.mapFieldFromClass(beanClass);
    }


    @Override
    protected Map<String,Integer> mapFieldsToIndex(ResultSetMetaData rsmd)throws SQLException{
        Map<String,Integer> colIndexMap = new HashMap<>();
        int cols = rsmd.getColumnCount();
        Map<String, String> columnToFieldMap = TableObjectMetaCache.getColumnToFieldMap(beanClass);
        Set<String> fieldNames = fieldMap.keySet();
        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnLabel(col);

            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(col);
            }
            String cachedFieldName = columnToFieldMap==null?null:columnToFieldMap.get(columnName.toLowerCase());

            if(StringUtils.isNotBlank(cachedFieldName)){
                colIndexMap.put(cachedFieldName,col);
            }else {
                final String generousColumnName = columnName.replace("_", "");
                for (String fieldName:fieldNames){
                    Field field = fieldMap.get(fieldName);
                    Column column = field.getAnnotation(Column.class);
                    if(column!=null){
                        String tblColName = column.value();
                        if(StringUtils.isBlank(tblColName)){
                            tblColName = field.getName();
                        }
                        if(columnName.equalsIgnoreCase(tblColName)){
                            colIndexMap.put(fieldName,col);
                            break;
                        }
                    }
                    if(columnName.equalsIgnoreCase(fieldName)||generousColumnName.equalsIgnoreCase(fieldName)){
                        colIndexMap.put(fieldName,col);
                        break;
                    }
                }
            }

        }
        return colIndexMap;
    }

    @Override
    protected <T> T populateBean(ResultSet rs, T bean, Map<String,Integer> fieldColIndexMap) throws SQLException {
        for(Map.Entry<String,Integer> entry:fieldColIndexMap.entrySet()){
            String fieldName = entry.getKey();
            Integer colIndex = entry.getValue();
            Field field = fieldMap.get(fieldName);
            if(field!=null){
                Class<?> fieldClass = field.getType();
                Object value = this.processColumn(rs, colIndex, fieldClass);
                if (value == null && fieldClass.isPrimitive()) {
                    value = primitiveDefaults.get(fieldClass);
                }
                try {
                    MiscUtil.setValue(bean,field,value);
                } catch (Exception e) {
                    throw new SQLException(e);
                }
            }
        }
        return bean;
    }
}
