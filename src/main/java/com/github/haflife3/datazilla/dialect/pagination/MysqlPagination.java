package com.github.haflife3.datazilla.dialect.pagination;

import java.util.List;

public class MysqlPagination implements Pagination{
    @Override
    public String paging(Integer pageNo, Integer pageSize, String sql, List<Object> values) {
        if(pageNo!=null&&pageSize!=null&&pageNo!=0&&pageSize!=0) {
            int fromIndex = (pageNo - 1) * pageSize;
            sql+=" limit ?,? ";
            values.add(fromIndex);
            values.add(pageSize);
        }
        return sql;
    }
}
