package com.github.haflife3.datazilla.dialect.pagination;

import java.util.List;

public interface OfflinePagination {
    <T> List<T> paginate(List<T> collection, Integer offset, Integer limit);
}
