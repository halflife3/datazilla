package com.github.haflife3.datazilla.dialect.pagination;


import com.github.haflife3.datazilla.misc.MiscUtil;

import java.util.List;

public class DefaultOfflinePagination implements OfflinePagination {
    @Override
    public <T> List<T> paginate(List<T> collection, Integer offset, Integer limit) {
        return MiscUtil.paginate(collection,offset,limit);
    }
}
