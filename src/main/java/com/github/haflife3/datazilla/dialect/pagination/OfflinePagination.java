package com.github.haflife3.datazilla.dialect.pagination;

import com.github.haflife3.datazilla.dialect.DatabaseTypeMatcher;

import java.util.List;

public interface OfflinePagination extends DatabaseTypeMatcher {
    <T> List<T> paginate(List<T> collection, Integer offset, Integer limit);
}
