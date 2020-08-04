package com.github.haflife3.test.fakedb;

import com.github.haflife3.datazilla.dialect.regulate.HsqlDbEntityRegulator;

public class FakeDbEntityRegulator extends HsqlDbEntityRegulator {
    @Override
    public String getDatabaseType() {
        return FakeDBConst.DB_TYPE;
    }
}
