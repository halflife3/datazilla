package com.github.haflife3.test.fakedb;

import com.github.haflife3.datazilla.dialect.typemapping.HsqlDbTypeMapper;

public class FakeDbTypeMapper extends HsqlDbTypeMapper {
    @Override
    public String getDatabaseType() {
        return FakeDBConst.DB_TYPE;
    }
}
