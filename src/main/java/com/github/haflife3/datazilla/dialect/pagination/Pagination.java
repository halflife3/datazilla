package com.github.haflife3.datazilla.dialect.pagination;

import com.github.haflife3.datazilla.dialect.DatabaseTypeMatcher;

import java.util.List;

public interface Pagination extends DatabaseTypeMatcher {
    String paging(Integer offset, Integer limit, String sql, List<Object> values);
}
