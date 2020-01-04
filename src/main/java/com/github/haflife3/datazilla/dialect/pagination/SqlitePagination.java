package com.github.haflife3.datazilla.dialect.pagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SqlitePagination implements Pagination{
    private static final Logger logger = LoggerFactory.getLogger(SqlitePagination.class);
    @Override
    public String paging(Integer pageNo, Integer pageSize, String sql, List<Object> values) {
        if(pageNo!=null&&pageSize!=null&&pageNo!=0&&pageSize!=0) {
            logger.warn("inefficient pagination solution for SQLite!");
            int fromIndex = (pageNo - 1) * pageSize;
            sql+=" limit ?,? ";
            values.add(fromIndex);
            values.add(pageSize);
        }
        return sql;
    }
}
