package com.github.haflife3.datazilla.pojo;

import java.util.List;

public class UpdateConditionBundle extends ConditionBundle {
    private List<FieldValuePair> values2Update;

    private UpdateConditionBundle(Builder builder) {
        setTargetTable(builder.targetTable);
        setConditionAndList(builder.conditionAndList);
        setConditionOrList(builder.conditionOrList);
        setValues2Update(builder.values2Update);
    }

    public List<FieldValuePair> getValues2Update() {
        return values2Update;
    }

    public void setValues2Update(List<FieldValuePair> values2Update) {
        this.values2Update = values2Update;
    }

    public static final class Builder {
        private String targetTable;
        private List<Cond> conditionAndList;
        private List<Cond> conditionOrList;
        private List<FieldValuePair> values2Update;

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

        public Builder values2Update(List<FieldValuePair> values2Update) {
            this.values2Update = values2Update;
            return this;
        }

        public UpdateConditionBundle build() {
            return new UpdateConditionBundle(this);
        }
    }
}
