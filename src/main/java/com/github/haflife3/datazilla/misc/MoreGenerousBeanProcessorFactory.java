package com.github.haflife3.datazilla.misc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MoreGenerousBeanProcessorFactory {

    private static final Map<String,MoreGenerousBeanProcessor> beanProcessorMap = new ConcurrentHashMap<>();

    public static MoreGenerousBeanProcessor populateBeanProcessor(Class<?> clazz){
        String clazzName = clazz.getName();
        if(beanProcessorMap.containsKey(clazzName)){
            return beanProcessorMap.get(clazzName);
        }else {
            MoreGenerousBeanProcessor beanProcessor = new MoreGenerousBeanProcessor(clazz);
            beanProcessorMap.put(clazzName,beanProcessor);
            return beanProcessor;
        }
    }
}
