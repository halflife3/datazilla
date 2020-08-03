/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.haflife3.datazilla.misc;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.ColumnHandler;
import org.apache.commons.dbutils.PropertyHandler;
import org.apache.commons.dbutils.handlers.columns.*;
import org.apache.commons.dbutils.handlers.properties.DatePropertyHandler;
import org.apache.commons.dbutils.handlers.properties.StringEnumPropertyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * customized to optimize ColumnHandler matching logic
 */
public class CustomBeanProcessor extends BeanProcessor {
    private static final Logger logger = LoggerFactory.getLogger(CustomBeanProcessor.class);

    /**
     * Set a bean's primitive properties to these defaults when SQL NULL
     * is returned.  These are the same as the defaults that ResultSet get*
     * methods return in the event of a NULL column.
     */
    protected static final Map<Class<?>, Object> primitiveDefaults = new HashMap<>();

    private static final List<ColumnHandler> defaultColumnHandlerList = Arrays.asList(
        new BooleanColumnHandler(),
        new ByteColumnHandler(),
        new DoubleColumnHandler(),
        new FloatColumnHandler(),
        new IntegerColumnHandler(),
        new LongColumnHandler(),
        new ShortColumnHandler(),
        new SQLXMLColumnHandler(),
        new StringColumnHandler(),
        new TimestampColumnHandler()
    );
    private static final List<ColumnHandler> columnHandlerList = new ArrayList<>();

    private static final List<PropertyHandler> defaultPropertyHandlerList = Arrays.asList(
        new DatePropertyHandler(),
        new StringEnumPropertyHandler()
    );

    private static final List<PropertyHandler> propertyHandlerList = new ArrayList<>();

    static {
        primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
        primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
        primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
        primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
        primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
        primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));

        columnHandlerList.addAll(defaultColumnHandlerList);
        ServiceLoader<ColumnHandler> columnHandlers = ServiceLoader.load(ColumnHandler.class);
        for (ColumnHandler columnHandler : columnHandlers) {
            if(!containsSameClassInstance(columnHandlerList,columnHandler)) {
                columnHandlerList.add(columnHandler);
            }
        }
        propertyHandlerList.addAll(defaultPropertyHandlerList);
        ServiceLoader<PropertyHandler> propertyHandlers = ServiceLoader.load(PropertyHandler.class);
        for (PropertyHandler propertyHandler : propertyHandlers) {
            if(!containsSameClassInstance(propertyHandlerList,propertyHandler)) {
                propertyHandlerList.add(propertyHandler);
            }
        }
        logger.info("columnHandlerList:"+columnHandlerList);
        logger.info("propertyHandlerList:"+propertyHandlerList);
    }

    private static boolean containsSameClassInstance(List<?> list,Object obj){
        boolean contains = false;
        Class<?> clazz = obj.getClass();
        if(list!=null){
            for (Object o : list) {
                Class<?> compareClass = o.getClass();
                if(compareClass.equals(clazz)){
                    contains = true;
                    break;
                }
            }
        }
        return contains;
    }

    /**
     * Convert a <code>ResultSet</code> into a <code>List</code> of JavaBeans.
     * This implementation uses reflection and <code>BeanInfo</code> classes to
     * match column names to bean property names. Properties are matched to
     * columns based on several factors:
     * &lt;br/&gt;
     * &lt;ol&gt;
     *     &lt;li&gt;
     *     The class has a writable property with the same name as a column.
     *     The name comparison is case insensitive.
     *     &lt;/li&gt;
     *
     *     &lt;li&gt;
     *     The column type can be converted to the property's set method
     *     parameter type with a ResultSet.get* method.  If the conversion fails
     *     (ie. the property was an int and the column was a Timestamp) an
     *     SQLException is thrown.
     *     &lt;/li&gt;
     * &lt;/ol&gt;
     *
     * <p>
     * Primitive bean properties are set to their defaults when SQL NULL is
     * returned from the <code>ResultSet</code>.  Numeric fields are set to 0
     * and booleans are set to false.  Object bean properties are set to
     * <code>null</code> when SQL NULL is returned.  This is the same behavior
     * as the <code>ResultSet</code> get* methods.
     * &lt;/p&gt;
     * @param <T> The type of bean to create
     * @param rs ResultSet that supplies the bean data
     * @param type Class from which to create the bean instance
     * @throws SQLException if a database access error occurs
     * @return the newly created List of beans
     */
    @Override
    public <T> List<T> toBeanList(ResultSet rs, Class<? extends T> type) throws SQLException {
        List<T> results = new ArrayList<T>();

        if (!rs.next()) {
            return results;
        }

        ResultSetMetaData rsmd = rs.getMetaData();
        Map<String,Integer> fieldColIndexMap = mapFieldsToIndex(rsmd);
        do {
            results.add(this.createBean(rs, type, fieldColIndexMap));
        } while (rs.next());

        return results;
    }

    private  <T> T createBean(ResultSet rs, Class<T> type,Map<String,Integer> fieldColIndexMap) throws SQLException {
        T bean = this.newInstance(type);
        return this.populateBean(rs,bean,fieldColIndexMap);
    }

    protected <T> T populateBean(ResultSet rs, T bean, Map<String,Integer> fieldColIndexMap) throws SQLException {
        throw new DBException("Not implemented!");
    }

    /**
     * Factory method that returns a new instance of the given Class.  This
     * is called at the start of the bean creation process and may be
     * overridden to provide custom behavior like returning a cached bean
     * instance.
     * @param <T> The type of object to create
     * @param c The Class to create an object from.
     * @return A newly created object of the Class.
     * @throws SQLException if creation failed.
     */
    @Override
    protected <T> T newInstance(Class<T> c) throws SQLException {
        try {
            return c.newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new SQLException(
                "Cannot create " + c.getName() + ": " + e.getMessage());

        }
    }

    protected Map<String,Integer> mapFieldsToIndex(ResultSetMetaData rsmd)throws SQLException{
        throw new DBException("Not implemented!");
    }

    /**
     * Convert a <code>ResultSet</code> column into an object.  Simple
     * implementations could just call <code>rs.getObject(index)</code> while
     * more complex implementations could perform type manipulation to match
     * the column's type to the bean property type.
     *
     * <p>
     * This implementation calls the appropriate <code>ResultSet</code> getter
     * method for the given property type to perform the type conversion.  If
     * the property type doesn't match one of the supported
     * <code>ResultSet</code> types, <code>getObject</code> is called.
     * </p>
     *
     * @param rs The <code>ResultSet</code> currently being processed.  It is
     * positioned on a valid row before being passed into this method.
     *
     * @param index The current column index being processed.
     *
     * @param propType The bean property type that this column needs to be
     * converted into.
     *
     * @throws SQLException if a database access error occurs
     *
     * @return The object from the <code>ResultSet</code> at the given column
     * index after optional type processing or <code>null</code> if the column
     * value was SQL NULL.
     */
    @Override
    protected Object processColumn(ResultSet rs, int index, Class<?> propType)
        throws SQLException {

        Object retval = rs.getObject(index);

        if ( !propType.isPrimitive() && retval == null ) {
            return null;
        }

        for (ColumnHandler handler : columnHandlerList) {
            if (handler.match(propType)) {
                retval = handler.apply(rs, index);
                break;
            }
        }

        return retval;

    }

}
