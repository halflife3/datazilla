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
        if(pageNo!=null&&pageSize!=null&&pageNo!=0&&pageSize!=0){
            int fromIndex = (pageNo - 1) * pageSize;
            offset(fromIndex,pageSize,needCount,orderConds);
        }
    }

    public static void offset(Integer offset, Integer limit, boolean needCount, OrderCond... orderConds){
        if(offset!=null&&limit!=null){
            GeneralThreadLocal.set("offset",offset);
            GeneralThreadLocal.set("limit",limit);
            GeneralThreadLocal.set("needCount",needCount);
            if(orderConds!=null){
                List<OrderCond> conds = Arrays.asList(orderConds);
                GeneralThreadLocal.set("orderConds",conds);
            }
        }
    }

    public static Integer getOffset(){
        return GeneralThreadLocal.get("offset");
    }

    public static Integer getLimit(){
        return GeneralThreadLocal.get("limit");
    }

    public static boolean needCount(){
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
        GeneralThreadLocal.unset("offset");
        GeneralThreadLocal.unset("limit");
        GeneralThreadLocal.unset("orderConds");
        GeneralThreadLocal.unset("needCount");
    }
}
