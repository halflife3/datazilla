package com.github.haflife3.test.hsqldb

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.dataobject.DummyTableHsqlDb
import com.github.haflife3.dataobject.DummyTableHsqlDbAlt
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class HsqlDbTest extends CommonTest{

    @Override
    protected List<Class<? extends DummyTable>> getRecordClass() {
        return [DummyTableHsqlDb, DummyTableHsqlDbAlt]
    }

    @Override
    protected String getDbType() {
        return DialectConst.HSQLDB
    }

    @Override
    protected String tableName() {
        return super.tableName()
    }

    @Override
    protected boolean idInt(){
        return true
    }

    @Test
    @Override
    void test(){
        super.test()
    }
}
