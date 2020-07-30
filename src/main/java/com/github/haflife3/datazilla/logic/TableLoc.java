package com.github.haflife3.datazilla.logic;


import com.github.haflife3.datazilla.annotation.Table;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class TableLoc {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableLoc.class);

    public static String findTableName(Class<?> type){
        return findTableNameByAnnotation(type);
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
                .filter(clazz -> !clazz.isSynthetic()&&!clazz.isInterface()&&clazz.isAnnotationPresent(Table.class))
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
}
