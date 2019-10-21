package com.github.haflife3.datazilla.dialect.regulate;

public class MysqlEntityRegulator implements EntityRegulator{
    @Override
    public String regulateTable(String table) {
        return table;
    }

    @Override
    public String regulateField(String field) {
        return field;
    }

    private String regulateEntity(String entity){
        String regulatedEntity = entity;
        if(bareEntity(entity)){
            regulatedEntity = "`"+entity+"`";
        }
        return regulatedEntity;
    }
}
