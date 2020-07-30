package com.github.haflife3.datazilla.dialect.pagination;



import java.util.List;

public class DummyOfflinePagination implements OfflinePagination {
    @Override
    public <T> List<T> paginate(List<T> collection, Integer offset, Integer limit) {
        return collection;
    }
}
