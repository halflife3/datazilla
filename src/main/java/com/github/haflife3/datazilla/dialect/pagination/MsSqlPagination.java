package com.github.haflife3.datazilla.dialect.pagination;

import java.util.List;

public class MsSqlPagination implements Pagination{
    @Override
    public String paging(Integer offset, Integer limit, String sql, List<Object> values) {
        if(offset!=null&&limit!=null) {
            sql+=" offset ? rows fetch next ? rows only";
            values.add(offset);
            values.add(limit);
        }
        return sql;
    }
}
