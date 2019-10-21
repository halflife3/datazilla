package com.github.haflife3.datazilla.misc;



import com.github.haflife3.datazilla.pojo.OrderCond;

import java.util.Arrays;
import java.util.List;

/**
 * @author halflife3
 * @date 2019/9/21
 */
public class PagingInjector {

    public static void fillParam(Integer pageNo, Integer pageSize, boolean needCount, OrderCond... orderConds){
        GeneralThreadLocal.set("pageNo",pageNo);
        GeneralThreadLocal.set("pageSize",pageSize);
        GeneralThreadLocal.set("needCount",needCount);
        if(orderConds!=null){
            List<OrderCond> conds = Arrays.asList(orderConds);
            GeneralThreadLocal.set("orderConds",conds);
        }
    }

    public static Integer getPageNo(){
        return GeneralThreadLocal.get("pageNo");
    }

    public static Integer getPageSize(){
        return GeneralThreadLocal.get("pageSize");
    }

    public static boolean isNeedCount(){
        Boolean needCount = GeneralThreadLocal.get("needCount");
        return needCount!=null&&needCount;
    }

    public static List<OrderCond> getOrderConds(){
        return GeneralThreadLocal.get("orderConds");
    }

    public static Integer getCount(){
        return GeneralThreadLocal.get("queryCount");
    }

    public static void setCount(int count){
        GeneralThreadLocal.set("queryCount",count);
    }

    public static void dropResult(){
        GeneralThreadLocal.unset("queryCount");
    }

    public static void unSet(){
        GeneralThreadLocal.unset("pageNo");
        GeneralThreadLocal.unset("pageSize");
        GeneralThreadLocal.unset("orderConds");
        GeneralThreadLocal.unset("needCount");
    }
}
