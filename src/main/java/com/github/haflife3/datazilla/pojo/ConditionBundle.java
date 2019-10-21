package com.github.haflife3.datazilla.pojo;

import java.util.List;

public class ConditionBundle {
    private String targetTable;
    private List<Cond> conditionAndList;
    private List<Cond> conditionOrList;

    public ConditionBundle() {
    }

    private ConditionBundle(Builder builder) {
        setTargetTable(builder.targetTable);
        setConditionAndList(builder.conditionAndList);
        setConditionOrList(builder.conditionOrList);
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public List<Cond> getConditionAndList() {
        return conditionAndList;
    }

    public void setConditionAndList(List<Cond> conditionAndList) {
        this.conditionAndList = conditionAndList;
    }

    public List<Cond> getConditionOrList() {
        return conditionOrList;
    }

    public void setConditionOrList(List<Cond> conditionOrList) {
        this.conditionOrList = conditionOrList;
    }

    public static final class Builder {
        private String targetTable;
        private List<Cond> conditionAndList;
        private List<Cond> conditionOrList;

        public Builder() {
        }

        public Builder targetTable(String targetTable) {
            this.targetTable = targetTable;
            return this;
        }

        public Builder conditionAndList(List<Cond> conditionAndList) {
            this.conditionAndList = conditionAndList;
            return this;
        }

        public Builder conditionOrList(List<Cond> conditionOrList) {
            this.conditionOrList = conditionOrList;
            return this;
        }

        public ConditionBundle build() {
            return new ConditionBundle(this);
        }
    }
}
