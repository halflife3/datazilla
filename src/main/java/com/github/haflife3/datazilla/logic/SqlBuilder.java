package com.github.haflife3.datazilla.logic;

import com.github.haflife3.datazilla.annotation.CondOpr;
import com.github.haflife3.datazilla.dialect.DialectFactory;
import com.github.haflife3.datazilla.dialect.operator.OprStore;
import com.github.haflife3.datazilla.dialect.pagination.Pagination;
import com.github.haflife3.datazilla.dialect.regulate.EntityRegulator;
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
    private static List<String> availableOrderByTypes = Arrays.asList("desc","asc");
    private EntityRegulator entityRegulator;
    private OprStore oprStore;
    private Pagination pagination;

    public SqlBuilder(String dbType) {
        this.oprStore = DialectFactory.getOprStore(dbType);
        this.pagination = DialectFactory.getPagination(dbType);
        this.entityRegulator = DialectFactory.getEntityRegulator(dbType);
    }

    private void fillWherePart(ConditionBundle cond, StringBuilder where, List<Object> values){
        if(cond!=null){
            List<Cond> conditionAndList = cond.getConditionAndList();
            if(CollectionUtils.isNotEmpty(conditionAndList)) {
                for(Cond unit:conditionAndList){
                    String field = regulateField(unit.getFieldName());
                    String operator = StringUtils.trimToEmpty(unit.getCompareOpr()).toLowerCase().replaceAll("\\s+", " ");
                    Object value = unit.getValue();
                    if(operator.equalsIgnoreCase("in") || operator.equalsIgnoreCase("not in")){
                        Collection inValueList = (Collection) value;
                        if(CollectionUtils.isNotEmpty(inValueList)){
                            where.append(" and "+field+" "+operator+" (");
                            for(Object valueTmp:inValueList){
                                where.append("?,");
                                values.add(valueTmp);
                            }
                            where.deleteCharAt(where.length()-1);
                            where.append(") ");
                        }
                    }else if(oprStore.getSingularOperators().contains(operator)){
                        where.append(" and "+field+" "+operator+" ");
                    }else{
                        where.append(" and "+field+" "+operator+" ?");
                        values.add(value);
                    }
                }
            }
            List<Cond> conditionOrList = cond.getConditionOrList();
            if(CollectionUtils.isNotEmpty(conditionOrList)){
                StringBuilder whereOr = new StringBuilder();
                for(Cond unit:conditionOrList){
                    String field = regulateField(unit.getFieldName());
                    String operator = StringUtils.trimToEmpty(unit.getCompareOpr()).toLowerCase().replaceAll("\\s+", " ");
                    Object value = unit.getValue();
                    if(operator.equalsIgnoreCase("in") || operator.equalsIgnoreCase("not in")){
                        Collection inValueList = (Collection) value;
                        if(CollectionUtils.isNotEmpty(inValueList)){
                            where.append(" or "+field+" "+operator+" (");
                            for(Object valueTmp:inValueList){
                                where.append("?,");
                                values.add(valueTmp);
                            }
                            where.deleteCharAt(where.length()-1);
                            where.append(") ");
                        }
                    }else if(operator.equalsIgnoreCase("is null") || operator.equalsIgnoreCase("is not null")){
                        where.append(" or "+field+" "+operator+" ");
                    }else{
                        whereOr.append(" or "+field+" "+operator+" ?");
                        values.add(value);
                    }
                }
                if(whereOr.length()>0) {
                    where.append(" and ("+whereOr.substring(3)+")");
                }
            }
        }
    }

    private void fillOrderByPart(List<OrderCond> orderConds, StringBuilder where){
        if(CollectionUtils.isNotEmpty(orderConds)){
            where.append(" order by ");
            for(OrderCond orderCond : orderConds){
                String orderByField = regulateField(orderCond.getOrderByField());
                String orderByType = orderCond.getOrderByType();
                where.append(" "+orderByField+" "+orderByType+",");
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

    private void checkConditionBundle(ConditionBundle cb){
        String targetTable = cb.getTargetTable();
        List<Cond> conditionAndList = cb.getConditionAndList();
        List<Cond> conditionOrList = cb.getConditionOrList();
        List<Cond> allList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(conditionAndList)) {
            allList.addAll(conditionAndList);
        }
        if(CollectionUtils.isNotEmpty(conditionOrList)) {
            allList.addAll(conditionOrList);
        }
        for(Cond unit:allList) {
            String field = unit.getFieldName();
            checkNoSemiColon(field);
            String operator = unit.getCompareOpr();
            if(!oprStore.getAllOperators().contains(StringUtils.trimToEmpty(operator).toLowerCase().replaceAll("\\s+", " "))){
                throw new DBException("Unknown Operator:"+operator);
            }
        }
        checkNoSemiColon(targetTable);
    }

    private void checkQueryConditionBundle(QueryConditionBundle qc){
        checkConditionBundle(qc);
        List<String> intendedFields = qc.getIntendedFields();
        if(CollectionUtils.isNotEmpty(intendedFields)) {
            checkNoSemiColon(intendedFields.toArray(new String[intendedFields.size()]));
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
        checkConditionBundle(uc);
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
        List<String> intendedFields = qc.getIntendedFields();
        if(intendedFields!=null&&!intendedFields.isEmpty()){
            for(String intendedField:intendedFields){
                select.append(" "+ regulateField(intendedField)+",");
            }
            select.deleteCharAt(select.length()-1);
            select.append(" from ");
        }else if(qc.isOnlyCount()){
            select.append(" count(*) as count from ");
        }else{
            select.append(" * from ");
        }
        select.append(" ").append(regulateTable(qc.getTargetTable())).append(" ");

        fillWherePart(qc,where,values);
        fillOrderByPart(qc.getOrderConds(),where);
        String baseSql = cleanSqlCond(select.append(where).toString());
        sp.setSql(pagination.paging(qc.getPageNo(),qc.getPageSize(),baseSql,values));
        sp.setValues(values.toArray());
        return sp;
    }

    public SqlPreparedBundle composeDelete(ConditionBundle cb){
        checkConditionBundle(cb);
        SqlPreparedBundle sp = new SqlPreparedBundle();
        List<Object> values= new ArrayList<Object>();
        StringBuilder delete = new StringBuilder("delete from ").append(regulateTable(cb.getTargetTable()));
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
        StringBuilder update = new StringBuilder("update ").append(regulateTable(uc.getTargetTable())).append(" ");
        List<FieldValuePair> values2Update = uc.getValues2Update();
        if(CollectionUtils.isNotEmpty(values2Update)){
            update.append(" set ");
            for (FieldValuePair pair : values2Update) {
                String field = regulateField(pair.getField());
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
        List<Cond> conds = new ArrayList<>();
        if(obj!=null){
            try {
                Class<?> clazz = obj.getClass();
                Collection<Field> fields = MiscUtil.mapFieldFromClass(clazz).values();
                for (Field field : fields) {
                    if(!field.isSynthetic()) {
                        field.setAccessible(true);
                        Object value = field.get(obj);
                        if (value != null && StringUtils.isNotBlank("" + value)) {
                            CondOpr condOpr = field.getAnnotation(CondOpr.class);
                            if (condOpr != null) {
                                if (!condOpr.ignore()) {
                                    String opr = StringUtils.trimToEmpty(condOpr.value()).toLowerCase().replaceAll("\\s+", " ");
                                    String fieldName = condOpr.fieldName();
                                    checkNoSemiColon(fieldName);
                                    if (StringUtils.isBlank(fieldName)) {
                                        fieldName = field.getName();
                                    }
                                    if (!oprStore.getAllOperators().contains(opr)) {
                                        throw new DBException("Unknown OrderByType:" + opr);
                                    }
                                    if (opr.contains("like")) {
                                        value = "%" + value + "%";
                                    }
                                    conds.add(new Cond(fieldName, opr, value));
                                }
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new DBException(e);
            }
        }
        return conds;
    }

    public String regulateTable(String table){
        return entityRegulator.regulateTable(table);
    }

    public String regulateField(String field){
        return entityRegulator.regulateField(field);
    }
}
