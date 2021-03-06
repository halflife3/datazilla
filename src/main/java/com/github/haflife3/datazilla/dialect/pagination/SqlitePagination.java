package com.github.haflife3.datazilla.dialect.pagination;

import com.github.haflife3.datazilla.dialect.DialectConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SqlitePagination implements Pagination{
    @Override
    public String getDatabaseType() {
        return DialectConst.SQLITE;
    }
    private static final Logger logger = LoggerFactory.getLogger(SqlitePagination.class);
    @Override
    public String paging(Integer offset, Integer limit, String sql, List<Object> values) {
        if(offset!=null&&limit!=null) {
            logger.warn("inefficient pagination solution for SQLite!");
            sql+=" limit ?,? ";
            values.add(offset);
            values.add(limit);
        }
        return sql;
    }
}
