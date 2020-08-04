package com.github.haflife3.test.fakedb;

import com.github.haflife3.datazilla.dialect.pagination.HsqlDbPagination;

public class FakeDbPagination extends HsqlDbPagination {
    @Override
    public String getDatabaseType() {
        return FakeDBConst.DB_TYPE;
    }
}
