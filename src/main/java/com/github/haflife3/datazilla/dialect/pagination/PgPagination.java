package com.github.haflife3.datazilla.dialect.pagination;

import java.util.List;

public class PgPagination implements Pagination {
    @Override
    public String paging(Integer pageNo, Integer pageSize, String sql, List<Object> values) {
        if(pageNo!=null&&pageSize!=null&&pageNo!=0&&pageSize!=0) {
            int fromIndex = (pageNo - 1) * pageSize;
            sql+=" limit ? offset ? ";
            values.add(pageSize);
            values.add(fromIndex);
        }
        return sql;
    }
}
