package com.github.haflife3.datazilla.pojo;

import java.util.List;

public class QueryConditionBundle extends ConditionBundle {
    private String resultClass;
    private List<String> intendedFields;
    private boolean onlyCount;
    private List<OrderCond> orderConds;
    private Integer pageNo;
    private Integer pageSize;

    public QueryConditionBundle() {
    }

    private QueryConditionBundle(Builder builder) {
        setTargetTable(builder.targetTable);
        setConditionAndList(builder.conditionAndList);
        setConditionOrList(builder.conditionOrList);
        setResultClass(builder.resultClass);
        setIntendedFields(builder.intendedFields);
        setOnlyCount(builder.onlyCount);
        setOrderConds(builder.orderConds);
        setPageNo(builder.pageNo);
        setPageSize(builder.pageSize);
    }

    public String getResultClass() {
        return resultClass;
    }

    public void setResultClass(String resultClass) {
        this.resultClass = resultClass;
    }

    public List<String> getIntendedFields() {
        return intendedFields;
    }

    public void setIntendedFields(List<String> intendedFields) {
        this.intendedFields = intendedFields;
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

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public static final class Builder {
        private List<String> intendedFields;
        private boolean onlyCount;
        private List<OrderCond> orderConds;
        private Integer pageNo;
        private Integer pageSize;
        private List<Cond> conditionAndList;
        private List<Cond> conditionOrList;
        private String resultClass;
        private String targetTable;

        public Builder() {
        }

        public Builder intendedFields(List<String> intendedFields) {
            this.intendedFields = intendedFields;
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

        public Builder pageNo(Integer pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
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

        public Builder resultClass(String resultClass) {
            this.resultClass = resultClass;
            return this;
        }

        public Builder targetTable(String targetTable) {
            this.targetTable = targetTable;
            return this;
        }
    }
}
