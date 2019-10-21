package com.github.haflife3.datazilla.dialect.pagination;

import java.util.List;

public interface Pagination {
    String paging(Integer pageNo, Integer pageSize, String sql, List<Object> values);
}
