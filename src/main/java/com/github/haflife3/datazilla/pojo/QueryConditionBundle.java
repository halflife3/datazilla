package com.github.haflife3.datazilla.pojo;

import java.util.List;

public class QueryConditionBundle extends ConditionBundle {
    private Class<?> resultClass;
    private List<String> selectColumns;
    private boolean onlyCount;
    private List<OrderCond> orderConds;
    private Integer offset;
    private Integer limit;

    public QueryConditionBundle() {
    }

    private QueryConditionBundle(Builder builder) {
        setTargetTable(builder.targetTable);
        setConditionAndList(builder.conditionAndList);
        setConditionOrList(builder.conditionOrList);
        setResultClass(builder.resultClass);
        setSelectColumns(builder.selectColumns);
        setOnlyCount(builder.onlyCount);
        setOrderConds(builder.orderConds);
        setOffset(builder.offset);
        setLimit(builder.limit);
    }

    public Class<?> getResultClass() {
        return resultClass;
    }

    public void setResultClass(Class<?> resultClass) {
        this.resultClass = resultClass;
    }

    public List<String> getSelectColumns() {
        return selectColumns;
    }

    public void setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
    }

    public boolean isOnlyCount() {
        return onlyCount;
    }

    public void setOnlyCount(boolean onlyCount) {
        this.onlyCount = onlyCount;
    }

    public List<OrderCond> getOrderConds() {
        return orderConds;
    }

    public void setOrderConds(List<OrderCond> orderConds) {
        this.orderConds = orderConds;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public static final class Builder {
        private List<String> selectColumns;
        private boolean onlyCount;
        private List<OrderCond> orderConds;
        private Integer offset;
        private Integer limit;
        private List<Cond> conditionAndList;
        private List<Cond> conditionOrList;
        private Class<?> resultClass;
        private String targetTable;

        public Builder() {
        }

        public Builder selectColumns(List<String> selectColumns) {
            this.selectColumns = selectColumns;
            return this;
        }

        public Builder onlyCount(boolean onlyCount) {
            this.onlyCount = onlyCount;
            return this;
        }

        public Builder orderByConds(List<OrderCond> orderConds) {
            this.orderConds = orderConds;
            return this;
        }

        public Builder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public Builder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public QueryConditionBundle build() {
            return new QueryConditionBundle(this);
        }

        public Builder conditionAndList(List<Cond> conditionAndList) {
            this.conditionAndList = conditionAndList;
            return this;
        }

        public Builder conditionOrList(List<Cond> conditionOrList) {
            this.conditionOrList = conditionOrList;
            return this;
        }

        public Builder resultClass(Class<?> resultClass) {
            this.resultClass = resultClass;
            return this;
        }

        public Builder targetTable(String targetTable) {
            this.targetTable = targetTable;
            return this;
        }
    }
}
