package com.github.haflife3.datazilla.dialect.pagination;

import java.util.List;

public interface Pagination {
    String paging(Integer offset, Integer limit, String sql, List<Object> values);
}
