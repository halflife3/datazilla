package com.github.haflife3.datazilla.misc;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

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

    public static <T> T cloneObject(Object orig,Class<T> targetBeanType)throws Exception{
        T targetInstance = null;
        if(orig!=null) {
            targetInstance = targetBeanType.newInstance();
            BeanUtils.copyProperties(targetInstance, orig);
        }
        return targetInstance;
    }

    public static <T> List<T> cloneObjects(List<?> origs, Class<T> targetBeanType) throws Exception {
        List<T> list = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(origs)){
            for (Object orig : origs) {
                list.add(cloneObject(orig,targetBeanType));
            }
        }
        return list;
    }

    public static List<String> extractStringListFromObjList(List list, String fieldName) throws Exception {
        List<String> l = new ArrayList<String>();
        if(list!=null){
            for(Object o:list){
                Field field = o.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                l.add((String)field.get(o));
            }
        }
        return l;
    }

    public static<T> List<T> extractFieldListFromObjList(List list, String fieldName) throws Exception {
        List<T> l = new ArrayList<T>();
        if(list!=null){
            for(Object o:list){
                Field field = o.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                l.add((T)field.get(o));
            }
        }
        return l;
    }

    public static<T> void setFieldValueListFromObjList(List<T> list, String fieldName,String value) throws Exception {

        if(list!=null){
            for(T o:list){
                Field field = o.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(o, value);
            }
        }
    }


    public static void setFieldValuesListFromObjList(List<?> list, String[] fieldNames,Object[] values) throws Exception {
        if(list!=null){
            if(fieldNames.length==values.length) {
                for (Object o : list) {
                    for (int i = 0; i < fieldNames.length; i++) {
                        Field field = o.getClass().getDeclaredField(fieldNames[i]);
                        field.setAccessible(true);
                        field.set(o, values[i]);
                    }
                }
            }
        }
    }
    public static <T,E> List<T> cleanList(List<T> toBeCleaned, List<E> flagList,String fieldName) throws Exception {
        List<T> cleanedList = new ArrayList<T>();
        if(toBeCleaned!=null&&flagList!=null){
            for(T o:toBeCleaned){
                Field field = o.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                E value = (E)field.get(o);
                if(flagList.contains(value)){
                    cleanedList.add(o);
                }
            }
        }
        return cleanedList;
    }
    public static <O, T> List<T> copyListBeanProperties(List<O> origList, Class<T> targetBeanType) throws Exception {
        List<T> targetList = null;
        if(origList!=null){
            targetList = new ArrayList<T>();
            for(O orig:origList){
                targetList.add(cloneObject(orig,targetBeanType));
            }
        }
        return targetList;
    }
    public static <T,K,V>Map<K,V> mapKVFieldFromList(List<T> list, String fieldNameKey, String fieldNameValue)throws Exception {
        Map<K,V> map = new HashMap<K, V>();
        if(list!=null){
            for(T o:list){
                Field fieldKey = o.getClass().getDeclaredField(fieldNameKey);
                fieldKey.setAccessible(true);
                Field fieldValue = o.getClass().getDeclaredField(fieldNameValue);
                fieldValue.setAccessible(true);
                map.put((K) fieldKey.get(o), (V)fieldValue.get(o));
            }
        }
        return map;
    }

    public static List<Map<String,Object>> mapListAsListMap(List list)throws Exception{
        List<Map<String,Object>> mapList = new ArrayList<>();
        if(list!=null){
            for(Object o:list){
                if(o!=null) {
                    Field[] fields = o.getClass().getDeclaredFields();
                    Map map = new HashMap();
                    for (Field field : fields) {
                        if(!field.isSynthetic()) {
                            field.setAccessible(true);
                            map.put(field.getName(), field.get(o));
                        }
                    }
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }
    public static <K,T>Map<K,T> mapFieldFromList(List<T> list, String fieldName)throws Exception {
        Map<K,T> map = new HashMap<K, T>();
        if(list!=null){
            for(T o:list){
                Field field = o.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                map.put((K) field.get(o), o);
            }
        }
        return map;
    }
    public static <K,T>Map<K,List<T>> mapFieldFromListToList(List<T> list,String fieldName)throws Exception {
        Map<K,List<T>> map = new HashMap<K, List<T>>();
        if(list!=null){
            for(T o:list){
                Field field = o.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                K key = (K) field.get(o);
                if(map.containsKey(key)){
                    map.get(key).add(o);
                }else{
                    List<T> tmpList = new ArrayList<T>();
                    tmpList.add(o);
                    map.put(key,tmpList);
                }
            }
        }
        return map;
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

    public static Map<String,Object> mapDomainObj(Object obj) throws Exception {
        return getFirst(mapListAsListMap(Arrays.asList(obj)));
    }
//    public static String encodeBase64Str(String plainText){
//        byte[] b=plainText.getBytes();
//        Base64 base64=new Base64();
//        b=base64.encode(b);
//        String s=new String(b);
//        return s;
//    }
//    public static String decodeBase64Str(String encodeStr){
//        byte[] b=encodeStr.getBytes();
//        Base64 base64=new Base64();
//        b=base64.decode(b);
//        String s=new String(b);
//        return s;
//    }

    public static String flatList(List list,String sep){
        StringBuilder sb = new StringBuilder();
        if(CollectionUtils.isNotEmpty(list)){
            for(Object o:list){
                if(o!=null){
                    sb.append(o.toString()).append(sep);
                }
            }
        }
        String flat = StringUtils.stripEnd(sb.toString(),sep);
        return flat;
    }

    public static <T> List<T> buildList(String listString, String sep,Class<T> targetClass)throws Exception{
        List<T> list = new ArrayList<>();
        if(StringUtils.isNotBlank(listString)){
            String[] split = listString.split(sep);
            for(String unit:split){
                if(targetClass==null||targetClass.equals(String.class)){
                    list.add((T)unit);
                }else if(targetClass.equals(Integer.class)){
                    list.add((T)Integer.valueOf(unit));
                }else if(targetClass.equals(Long.class)){
                    list.add((T)Long.valueOf(unit));
                }else if(targetClass.equals(Double.class)){
                    list.add((T)Double.valueOf(unit));
                }else if(targetClass.equals(Boolean.class)){
                    list.add((T)Boolean.valueOf(unit));
                }else if(targetClass.equals(Short.class)){
                    list.add((T)Short.valueOf(unit));
                }
            }
        }
        return list;
    }

    public static List<String> buildStringList(String listString, String sep) throws Exception {
        return buildList(listString,sep,String.class);
    }

    public static void assertFieldNotNull(List<String> allowNullFields,List objects)throws Exception{
        if(objects!=null){
            for(Object o:objects){
                if(o!=null){
                    Field[] fields = o.getClass().getDeclaredFields();
                    for(Field field:fields){
                        if(!field.isSynthetic()) {
                            field.setAccessible(true);
                            String name = field.getName();
                            if (CollectionUtils.isNotEmpty(allowNullFields) && !allowNullFields.contains(name)) {
                                if (field.get(o) == null) {
                                    throw new Exception(name + " can't be null");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static List<String> getFieldNames(Class clazz){
        List<String> list = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field:fields) {
            if(!field.isSynthetic()) {
                field.setAccessible(true);
                String name = field.getName();
                list.add(name);
            }
        }
        return list;
    }

    public static void setValue(Object target,String fieldName,Object value) throws Exception {
        if(target!=null){
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target,value);
        }
    }

    public static void fillNullFieldsWithDefault(Map<String,Object> defaultValueMap,List objects)throws Exception{
        if(objects!=null){
            for(Object o:objects){
                if(o!=null){
                    Field[] fields = o.getClass().getDeclaredFields();
                    for(Field field:fields){
                        field.setAccessible(true);
                        String name = field.getName();
                        if(field.get(o)==null){
                            field.set(o,defaultValueMap.get(name));
                        }
                    }
                }
            }
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

    public static <T> T extractFieldValueFromObj(Object o,Map<String,Field> fieldMap,String fieldName) throws Exception {
        T t = null;
        if(o!=null&& MapUtils.isNotEmpty(fieldMap)){
            if(fieldMap.containsKey(fieldName)){
                Object value = fieldMap.get(fieldName).get(o);
                if(value!=null) {
                    t = (T) value;
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
        }
        return fields;
    }

    public static Field getField(Class<?> clazz,String fieldName){
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            Field[] declaredFields = c.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if(fieldName.equals(declaredField.getName())){
                    return declaredField;
                }
            }
        }
        return null;
    }
    public static <T> List<T> paginate(List<T> collection, Integer pageNo, Integer pageSize){
        if(CollectionUtils.isNotEmpty(collection)&&pageNo!=null&&pageSize!=null&&pageNo!=0&&pageSize!=0) {
            int fromIndex = (pageNo - 1) * pageSize;
            return collection.stream()
                .skip(fromIndex)
                .limit(pageSize)
                .collect(Collectors.toList());
        }else {
            return collection;
        }
    }
}
