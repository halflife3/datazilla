package com.github.haflife3.datazilla.misc;

import com.github.haflife3.datazilla.pojo.OrderCond;
import org.apache.commons.lang3.StringUtils;

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

    public static void offset(Integer offset, Integer limit, boolean needCount, OrderCond... orderConds){
        PagingInjector.offset(offset,limit,needCount,orderConds);
    }

    public static void selectColumns(String ... selectColumns){
        if(selectColumns!=null&&selectColumns.length>0){
            GeneralThreadLocal.set("selectColumns", Arrays.asList(selectColumns));
        }
    }

    public static void sqlId(String sqlId){
        if(StringUtils.isNotBlank(sqlId)){
            GeneralThreadLocal.set("sqlId", sqlId);
        }
    }

    public static String getSqlId(){
        return GeneralThreadLocal.get("sqlId");
    }

    public static List<String> getSelectColumns(){
        return GeneralThreadLocal.get("selectColumns");
    }

    public static void unSet(){
        PagingInjector.unSet();
        GeneralThreadLocal.unset("selectColumns");
    }

    public static void unsetSqlId(){
        GeneralThreadLocal.unset("sqlId");
    }
}
