package com.github.haflife3.datazilla.logic;

import com.github.haflife3.datazilla.CoreRunner;
import com.github.haflife3.datazilla.annotation.CondOpr;
import com.github.haflife3.datazilla.dialect.pagination.Pagination;
import com.github.haflife3.datazilla.misc.DBException;
import com.github.haflife3.datazilla.misc.MiscUtil;
import com.github.haflife3.datazilla.pojo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SqlBuilder {
    private static final List<String> availableOrderByTypes = Arrays.asList("desc","asc");
    private final Pagination pagination;

    public SqlBuilder(CoreRunner coreRunner) {
        this.pagination = coreRunner.getPagination();
    }

    private void fillWherePart(ConditionBundle cond, StringBuilder where, List<Object> values){
        if(cond==null){
            return;
        }
        List<Cond> conditionAndList = cond.getConditionAndList();
        if(CollectionUtils.isNotEmpty(conditionAndList)) {
            for(Cond unit:conditionAndList){
                String field = unit.getColumnName();
                String operator = StringUtils.trimToEmpty(unit.getCompareOpr()).toLowerCase().replaceAll("\\s+", " ");
                Object value = unit.getValue();
                if(operator.equalsIgnoreCase("in") || operator.equalsIgnoreCase("not in")){
                    Collection inValueList = null;
                    if(value.getClass().isArray()){
                        inValueList = MiscUtil.fromArray(value);
                    }else if(value instanceof Collection){
                        inValueList = (Collection) value;
                    }
                    if(CollectionUtils.isNotEmpty(inValueList)){
                        where.append(" and ").append(field).append(" ").append(operator).append(" (");
                        for(Object valueTmp:inValueList){
                            where.append("?,");
                            values.add(valueTmp);
                        }
                        where.deleteCharAt(where.length()-1);
                        where.append(") ");
                    }
                }else if((operator.equalsIgnoreCase("between") || operator.equalsIgnoreCase("not between"))&&value!=null){
                    Object v1 = null;
                    Object v2 = null;
                    List listValue = null;
                    if(value.getClass().isArray()){
                        listValue = MiscUtil.fromArray(value);
                    }else if(value instanceof List){
                        listValue = (List) value;
                    }
                    if(CollectionUtils.isNotEmpty(listValue)&&listValue.size()==2){
                        v1 = listValue.get(0);
                        v2 = listValue.get(1);
                    }
                    if(v1!=null && v2!=null){
                        where.append(" and ").append(field).append(" ").append(operator).append(" ? and ? ");
                        values.add(v1);
                        values.add(v2);
                    }
                }else if(value==null){
                    where.append(" and ").append(field).append(" ").append(operator).append(" ");
                }else if(value instanceof Null){
                    where.append(" and ").append(field).append(" is null ");
                }else{
                    where.append(" and ").append(field).append(" ").append(operator).append(" ?");
                    values.add(value);
                }
            }
        }
        List<Cond> conditionOrList = cond.getConditionOrList();
        if(CollectionUtils.isNotEmpty(conditionOrList)){
            StringBuilder whereOr = new StringBuilder();
            for(Cond unit:conditionOrList){
                String field = unit.getColumnName();
                String operator = StringUtils.trimToEmpty(unit.getCompareOpr()).toLowerCase().replaceAll("\\s+", " ");
                Object value = unit.getValue();
                if((operator.equalsIgnoreCase("in") || operator.equalsIgnoreCase("not in"))&&value!=null){
                    Collection inValueList = null;
                    if(value.getClass().isArray()){
                        inValueList = MiscUtil.fromArray(value);
                    }else if(value instanceof Collection){
                        inValueList = (Collection) value;
                    }
                    if(CollectionUtils.isNotEmpty(inValueList)){
                        whereOr.append(" or ").append(field).append(" ").append(operator).append(" (");
                        for(Object valueTmp:inValueList){
                            whereOr.append("?,");
                            values.add(valueTmp);
                        }
                        whereOr.deleteCharAt(whereOr.length()-1);
                        whereOr.append(") ");
                    }
                }else if((operator.equalsIgnoreCase("between") || operator.equalsIgnoreCase("not between"))&&value!=null){
                    Object v1 = null;
                    Object v2 = null;
                    List listValue = null;
                    if(value.getClass().isArray()){
                        listValue = MiscUtil.fromArray(value);
                    }else if(value instanceof List){
                        listValue = (List) value;
                    }
                    if(CollectionUtils.isNotEmpty(listValue)&&listValue.size()==2){
                        v1 = listValue.get(0);
                        v2 = listValue.get(1);
                    }
                    if(v1!=null && v2!=null){
                        whereOr.append(" and ").append(field).append(" ").append(operator).append(" ? and ? ");
                        values.add(v1);
                        values.add(v2);
                    }
                }else if(value==null){
                    whereOr.append(" or ").append(field).append(" ").append(operator).append(" ");
                }else if(value instanceof Null){
                    whereOr.append(" or ").append(field).append(" is null ");
                }else{
                    whereOr.append(" or ").append(field).append(" ").append(operator).append(" ?");
                    values.add(value);
                }
            }
            if(whereOr.length()>0) {
                where.append(" and (").append(whereOr.substring(3)).append(")");
            }
        }
    }

    private void fillOrderByPart(List<OrderCond> orderConds, StringBuilder where){
        if(CollectionUtils.isNotEmpty(orderConds)){
            where.append(" order by ");
            for(OrderCond orderCond : orderConds){
                String orderByField = orderCond.getOrderByField();
                String orderByType = orderCond.getOrderByType();
                where.append(" ").append(orderByField).append(" ").append(orderByType).append(",");
            }
            where.deleteCharAt(where.length()-1);
        }
    }

    private void checkNoSemiColon(String ... entities){
        if(entities!=null){
            for (String entity : entities) {
                if(entity.contains(";")){
                    throw new DBException("invalid entity:"+entity);
                }
            }
        }
    }

    private void checkQueryConditionBundle(QueryConditionBundle qc){
        List<String> selectColumns = qc.getSelectColumns();
        if(CollectionUtils.isNotEmpty(selectColumns)) {
            checkNoSemiColon(selectColumns.toArray(new String[0]));
        }
        List<OrderCond> orderConds = qc.getOrderConds();
        if(CollectionUtils.isNotEmpty(orderConds)){
            for (OrderCond orderCond : orderConds) {
                String orderByField = orderCond.getOrderByField();
                checkNoSemiColon(orderByField);
                String orderByType = orderCond.getOrderByType();
                if(StringUtils.isNotBlank(orderByType)){
                    if(!availableOrderByTypes.contains(StringUtils.trimToEmpty(orderByType.toLowerCase()))){
                        throw new DBException("Unknown OrderByType:"+orderByType);
                    }
                }
            }
        }
    }

    private void checkUpdateConditionBundle(UpdateConditionBundle uc){
        List<FieldValuePair> values2Update = uc.getValues2Update();
        if(CollectionUtils.isNotEmpty(values2Update)){
            for (FieldValuePair pair : values2Update) {
                checkNoSemiColon(pair.getField());
            }
        }
    }

    private static String cleanSqlCond(String sql){
        return sql.replaceAll("where 1=1\\s+and","where");
    }

    public SqlPreparedBundle composeSelect(QueryConditionBundle qc){
        checkQueryConditionBundle(qc);
        SqlPreparedBundle sp = new SqlPreparedBundle();
        StringBuilder where = new StringBuilder(" where 1=1 ");
        List<Object> values= new ArrayList<Object>();
        StringBuilder select = new StringBuilder("select ");
        List<String> selectColumns = qc.getSelectColumns();
        if(selectColumns!=null&&!selectColumns.isEmpty()){
            for(String intendedField:selectColumns){
                select.append(" ").append(intendedField).append(",");
            }
            select.deleteCharAt(select.length()-1);
            select.append(" from ");
        }else if(qc.isOnlyCount()){
            select.append(" count(*) as count from ");
        }else{
            select.append(" * from ");
        }
        select.append(" ").append(qc.getTargetTable()).append(" ");

        fillWherePart(qc,where,values);
        fillOrderByPart(qc.getOrderConds(),where);
        String baseSql = cleanSqlCond(select.append(where).toString());
        sp.setSql(pagination.paging(qc.getOffset(),qc.getLimit(),baseSql,values));
        sp.setValues(values.toArray());
        return sp;
    }

    public SqlPreparedBundle composeDelete(ConditionBundle cb){
        SqlPreparedBundle sp = new SqlPreparedBundle();
        List<Object> values= new ArrayList<>();
        StringBuilder delete = new StringBuilder("delete from ").append(cb.getTargetTable());
        StringBuilder where = new StringBuilder(" where 1=1 ");
        fillWherePart(cb,where,values);
        sp.setSql(cleanSqlCond(delete.append(where).toString()));
        sp.setValues(values.toArray());
        return sp;
    }

    public SqlPreparedBundle composeUpdate(UpdateConditionBundle uc){
        checkUpdateConditionBundle(uc);
        SqlPreparedBundle sp = new SqlPreparedBundle();
        StringBuilder where = new StringBuilder(" where 1=1 ");
        List<Object> values= new ArrayList<Object>();
        StringBuilder update = new StringBuilder("update ").append(uc.getTargetTable()).append(" ");
        List<FieldValuePair> values2Update = uc.getValues2Update();
        if(CollectionUtils.isNotEmpty(values2Update)){
            update.append(" set ");
            for (FieldValuePair pair : values2Update) {
                String field = pair.getField();
                Object value = pair.getValue();
                update.append(field).append("=?,");
                values.add(value);
            }
            update.deleteCharAt(update.length()-1);
            fillWherePart(uc,where,values);
            sp.setSql(cleanSqlCond(update.append(where).toString()));
            sp.setValues(values.toArray());
        }
        return sp;
    }

    public List<Cond> buildConds(Object obj){
        if(obj==null){
            return null;
        }
        List<Cond> conds = new ArrayList<>();
        try {
            Class<?> clazz = obj.getClass();
            List<Field> fields = MiscUtil.getAllFields(clazz);
            for (Field field : fields) {
                if(field.isSynthetic()) {
                   continue;
                }
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value == null || StringUtils.isBlank("" + value)) {
                    continue;
                }
                if(value instanceof Collection && CollectionUtils.isEmpty((Collection)value)){
                    continue;
                }
                CondOpr condOpr = field.getAnnotation(CondOpr.class);
                if (condOpr == null) {
                    continue;
                }
                String opr = StringUtils.trimToEmpty(condOpr.value()).toLowerCase().replaceAll("\\s+", " ");
                String columnName = condOpr.columnName();
                if(StringUtils.isBlank(columnName)){
                    columnName = field.getName();
                }
                checkNoSemiColon(columnName);
                if (StringUtils.isBlank(columnName)) {
                    columnName = field.getName();
                }
                if (opr.contains("like")) {
                    value = "%" + value + "%";
                }
                conds.add(new Cond(columnName, opr, value));
            }
        } catch (IllegalAccessException e) {
            throw new DBException(e);
        }
        return conds;
    }
}
