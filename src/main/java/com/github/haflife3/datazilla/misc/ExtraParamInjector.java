package com.github.haflife3.datazilla.misc;

import com.github.haflife3.datazilla.pojo.OrderCond;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jianfeng.Mao2
 * @date 2020/6/28
 */
public class ExtraParamInjector {

    public static void paging(Integer pageNo, Integer pageSize, boolean needCount, OrderCond... orderConds){
        PagingInjector.fillParam(pageNo,pageSize,needCount,orderConds);
    }

    public static void selectColumns(String ... selectColumns){
        if(selectColumns!=null&&selectColumns.length>0){
            GeneralThreadLocal.set("selectColumns", Arrays.asList(selectColumns));
        }
    }

    public static List<String> getSelectColumns(){
        return GeneralThreadLocal.get("selectColumns");
    }

    public static void unSet(){
        PagingInjector.unSet();
        GeneralThreadLocal.unset("selectColumns");
    }
}
