package com.github.haflife3.datazilla.misc;

import java.util.HashMap;
import java.util.Map;

public class GeneralThreadLocal {
    private static final ThreadLocal<Map<String,Object>> COMMON_TL = new ThreadLocal<>();

    public static void set(Map<String,Object> map){
        COMMON_TL.set(map);
    }
    public static void set(String key,Object value) {
        Map<String, Object> map = COMMON_TL.get();
        if(map==null){
            map = new HashMap<>();
            map.put(key,value);
            COMMON_TL.set(map);
        }else {
            get().put(key,value);
        }
    }

    public static void unset() {
        COMMON_TL.remove();
    }

    public static void unset(String key) {
        Map<String, Object> objectMap = get();
        if(objectMap!=null){
            objectMap.remove(key);
        }
    }

    public static Map<String,Object> get(){
        return COMMON_TL.get();
    }

    public static  <T> T get(String key) {
        Map<String, Object> map = get();
        Object o = map!=null?map.get(key):null;
        return o!=null?(T) o:null;
    }

    public static boolean containsKey(String key){
        Map<String, Object> map = get();
        return map!=null&&map.containsKey(key);
    }

}
