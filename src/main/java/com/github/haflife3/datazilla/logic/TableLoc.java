package com.github.haflife3.datazilla.logic;


import com.github.haflife3.datazilla.annotation.Table;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    public static List<Class<?>> findClasses(String table,String packageName) throws Exception {
        return findClasses(table,tableClasses(packageName));
    }

    public static List<Class<?>> findClasses(String table, Collection<Class<?>> tableClasses) throws Exception {
        List<Class<?>> clazzList = new ArrayList<>();
        for (Class<?> aClass : tableClasses) {
            String tableName = findTableName(aClass);
            if(table.equalsIgnoreCase(tableName)){
                clazzList.add(aClass);
            }
        }
        return clazzList;
    }
}
