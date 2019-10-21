package com.github.haflife3.datazilla.logic;


import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.misc.DBException;
import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class TableLoc {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableLoc.class);

    public static String findTableName(Class<?> type){
        String tableName = findTableNameByAnnotation(type);
        try {
            if(StringUtils.isBlank(tableName)) {
                if (existFetchTableMethod(type)) {
                    Method method = type.getDeclaredMethod("fetchTableName");
                    tableName = (String) method.invoke(type.newInstance());
                }
            }
        } catch (Exception e) {
            throw new DBException(e);
        }
        return tableName;
    }
    public static String findTableNameByAnnotation(Class<?> type){
        String tableName = null;
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            boolean present = c.isAnnotationPresent(Table.class);
            if(present){
                Table table = c.getAnnotation(Table.class);
                tableName = table.value();
                break;
            }
        }
        return tableName;
    }

    public static Set<Class<?>> tableClasses(String packageName)throws Exception{
        return new Reflections(packageName, new SubTypesScanner(false))
                .getSubTypesOf(Object.class)
                .stream()
                .filter(clazz -> !clazz.isSynthetic()&&!clazz.isInterface()&&(existFetchTableMethod(clazz)||clazz.isAnnotationPresent(Table.class)))
                .collect(Collectors.toSet());
    }

    public static Class<?> findClass(String table,String packageName) throws Exception {
        return findClass(table,tableClasses(packageName));
    }

    public static Class<?> findClass(String table,Collection<Class<?>> tableClasses) throws Exception {
        Class<?> clazz = null;
        for (Class<?> aClass : tableClasses) {
            String tableName = findTableName(aClass);
            if(table.equalsIgnoreCase(tableName)){
                clazz = aClass;
                break;
            }
        }
        return clazz;
    }

    private static  boolean existFetchTableMethod(Class<?> type){
        boolean methodExist = false;
        Method[] declaredMethods = type.getDeclaredMethods();
        if (declaredMethods != null) {
            for (Method declaredMethod : declaredMethods) {
                String name = declaredMethod.getName();
                int parameterCount = declaredMethod.getParameterCount();
                if (name.equals("fetchTableName") && parameterCount == 0) {
                    methodExist = true;
                }
            }

        }
        return methodExist;
    }

    public static void main(String[] args) {
        String rt = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "user_id".toUpperCase());
        System.out.println(rt);
    }
}
