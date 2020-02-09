package com.github.haflife3.datazilla.misc;

import com.github.haflife3.datazilla.annotation.TblField;
import com.github.haflife3.datazilla.logic.TableObjectMetaCache;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

public class MoreGenerousBeanProcessor extends CustomBeanProcessor {
    private Class<?> beanClass;

    /**
     * Default constructor.
     */
    public MoreGenerousBeanProcessor(Class<?> beanClass) {
        super();
        this.beanClass = beanClass;
    }

    @Override
    protected int[] mapColumnsToProperties(final ResultSetMetaData rsmd,
                                           final PropertyDescriptor[] props) throws SQLException {

        final int cols = rsmd.getColumnCount();
        final int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        Map<String, String> columnToFieldMap = TableObjectMetaCache.getColumnToFieldMap(beanClass);
        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnLabel(col);

            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(col);
            }
            String cachedFieldName = columnToFieldMap==null?null:columnToFieldMap.get(columnName.toLowerCase());

            final String generousColumnName = columnName.replace("_", "");

            for (int i = 0; i < props.length; i++) {
                final String propName = props[i].getName();

                // see if either the column name, or the generous one matches
                if (matchTblField(columnName,propName,cachedFieldName)||columnName.equalsIgnoreCase(propName) ||
                        generousColumnName.equalsIgnoreCase(propName)) {
                    columnToProperty[col] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }

    private boolean matchTblField(String columnName,String propName,String cachedFieldName){
        boolean match = false;
        if(StringUtils.isNotBlank(cachedFieldName)){
            return propName.equals(cachedFieldName);
        }
        Field field = MiscUtil.getField(beanClass, propName);
        if(field!=null){
            TblField tblField = field.getAnnotation(TblField.class);
            if(tblField!=null){
                String tblFieldValue = tblField.value();
                if(StringUtils.isBlank(tblFieldValue)){
                    tblFieldValue = field.getName();
                }
                match = tblFieldValue.equalsIgnoreCase(columnName);
            }
        }
        return match;
    }
}
