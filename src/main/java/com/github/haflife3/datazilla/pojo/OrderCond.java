package com.github.haflife3.datazilla.pojo;

public class OrderCond {
    private String orderByField;
    private String orderByType = "asc";

    public OrderCond() {
    }

    public OrderCond(String orderByField) {
        this.orderByField = orderByField;
    }

    public OrderCond(String orderByField, String orderByType) {
        this.orderByField = orderByField;
        this.orderByType = orderByType;
    }

    public String getOrderByField() {
        return orderByField;
    }

    public void setOrderByField(String orderByField) {
        this.orderByField = orderByField;
    }

    public String getOrderByType() {
        return orderByType;
    }

    public void setOrderByType(String orderByType) {
        this.orderByType = orderByType;
    }
}
