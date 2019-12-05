package com.github.haflife3.test.pg

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.dataobject.DummyTablePG
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class PgTest extends CommonTest{

    @Override
    protected Class<? extends DummyTable> getRecordClass() {
        return DummyTablePG.class
    }

    @Override
    protected String getDbType() {
        return DialectConst.PG
    }

    @Override
    protected String tableName() {
        return super.tableName()
    }

    @Test
    @Override
    void test(){
        super.test()
    }
}
