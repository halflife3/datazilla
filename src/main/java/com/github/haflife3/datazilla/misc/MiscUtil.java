package com.github.haflife3.datazilla.misc;

import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by maojianfeng on 9/12/16.
 */
public class MiscUtil {

    public static <T> T getFirst(Collection<? extends T> coll){
        T t = null;
        if(CollectionUtils.isNotEmpty(coll)){
            Iterator<? extends T> iterator = coll.iterator();
            t = iterator.hasNext() ? iterator.next() : null;
        }
        return t;
    }

    public static Map<String,Object> mapObject(Object o)throws Exception {
        Map<String,Object> map = new HashMap<>();
        if(o!=null){
            Field[] fields = o.getClass().getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                if(!field.isSynthetic()) {
                    map.put(field.getName(), field.get(o));
                }
            }
        }
        return map;
    }

    public static Map<String,Field> mapFieldFromObj(Object o){
        return mapFieldFromObj(o,true);
    }
    public static Map<String,Field> mapFieldFromObj(Object o,boolean caseSensitive){
        Class<?> type = o.getClass();
        return mapFieldFromClass(type,caseSensitive);
    }

    public static Map<String,Field> mapFieldFromClass(Class<?> type){
        return mapFieldFromClass(type,true);
    }
    public static Map<String,Field> mapFieldFromClass(Class<?> type,boolean caseSensitive){
        Map<String,Field> map = null;
        if(caseSensitive){
            map = new HashMap<>();
        }else {
            map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        }
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            Field[] declaredFields = c.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if(field.isSynthetic()) {
                    continue;
                }
                String name = field.getName();
                if(!map.containsKey(name)) {
                    map.put(name, field);
                }
            }
        }
        return map;
    }

    public static void setValue(Object target,String fieldName,Object value) throws Exception {
        if(target!=null){
            setValue(target,target.getClass().getDeclaredField(fieldName),value);
        }
    }

    public static void setValue(Object target,Field field,Object value) throws Exception {
        if(target!=null){
            field.setAccessible(true);
            field.set(target,value);
        }
    }

    public static <T> T extractFieldValueFromObj(Object o,String fieldName) throws Exception {
        T t = null;
        if(o!=null){
            Class<?> type = o.getClass();
            outer:for (Class<?> c = type; c != null; c = c.getSuperclass()) {
                Field[] declaredFields = c.getDeclaredFields();
                for(Field field:declaredFields) {
                    field.setAccessible(true);
                    String name = field.getName();
                    if(name.equals(fieldName)){
                        Object value = field.get(o);
                        if(value!=null) {
                            t = (T) value;
                        }
                        break outer;
                    }
                }
            }
        }
        return t;
    }

    public static List<Field> getAllFields(Class<?> clazz){
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            Field[] declaredFields = c.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            fields.removeIf(Field::isSynthetic);
        }
        return fields;
    }

    public static List<Object> fromArray(Object array){
        List<Object> list = new ArrayList<>();
        if(array!=null){
            int length = Array.getLength(array);
            for (int i = 0; i < length; i ++) {
                Object arrayElement = Array.get(array, i);
                list.add(arrayElement);
            }
        }
        return list;
    }

    public static Field getField(Class<?> clazz,String fieldName){
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            Field[] declaredFields = c.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if(!declaredField.isSynthetic()&&fieldName.equals(declaredField.getName())){
                    return declaredField;
                }
            }
        }
        return null;
    }
    public static <T> List<T> paginate(List<T> collection, Integer offset, Integer limit){
        if(CollectionUtils.isNotEmpty(collection)&&offset!=null&&limit!=null) {
            return collection.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        }else {
            return collection;
        }
    }
}
