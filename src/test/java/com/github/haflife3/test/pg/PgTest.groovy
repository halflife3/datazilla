package com.github.haflife3.test.pg

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.dataobject.DummyTablePG
import com.github.haflife3.dataobject.DummyTablePGAlt
import com.github.haflife3.dataobject.DummyTablePGGv
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class PgTest extends CommonTest{

    @Override
    protected List<Class<? extends DummyTable>> getRecordClass() {
        return [DummyTablePG, DummyTablePGAlt, DummyTablePGGv]
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
