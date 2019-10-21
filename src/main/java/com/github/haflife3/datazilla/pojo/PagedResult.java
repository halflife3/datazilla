package com.github.haflife3.datazilla.pojo;

import java.util.List;

public class PagedResult<T>{
    private int totalCount;
    private List<T> result;

    public PagedResult() {
    }

    public PagedResult(int totalCount, List<T> result) {
        this.totalCount = totalCount;
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}
