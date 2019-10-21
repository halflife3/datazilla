package com.github.haflife3.datazilla.dialect.pagination;

import java.util.List;

public class DefaultPagination implements Pagination {
    @Override
    public String paging(Integer pageNo, Integer pageSize, String sql, List<Object> values) {
        return sql;
    }
}
