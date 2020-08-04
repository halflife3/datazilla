package com.github.haflife3.test.fakedb;

import com.github.haflife3.datazilla.dialect.batch.HsqlDbBatchInserter;

public class FakeDbBatchInserter extends HsqlDbBatchInserter {
    @Override
    public String getDatabaseType() {
        return FakeDBConst.DB_TYPE;
    }
}
