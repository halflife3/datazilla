package com.github.haflife3.datazilla.columnhandler;

import org.apache.commons.dbutils.ColumnHandler;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author halflife3
 * @date 2019/9/26
 */
public class BigDecimalHandler implements ColumnHandler {
    @Override
    public boolean match(Class<?> propType) {
        if(propType.equals(BigDecimal.class)){
            return true;
        }
        return false;
    }

    @Override
    public Object apply(ResultSet rs, int columnIndex) throws SQLException {
        return new BigDecimal(rs.getDouble(columnIndex));
    }
}
