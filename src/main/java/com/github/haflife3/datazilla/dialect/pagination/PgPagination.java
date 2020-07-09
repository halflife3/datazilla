package com.github.haflife3.datazilla.dialect.pagination;

import java.util.List;

public class PgPagination implements Pagination {
    @Override
    public String paging(Integer offset, Integer limit, String sql, List<Object> values) {
        if(offset!=null&&limit!=null) {
            sql+=" limit ? offset ? ";
            values.add(limit);
            values.add(offset);
        }
        return sql;
    }
}
