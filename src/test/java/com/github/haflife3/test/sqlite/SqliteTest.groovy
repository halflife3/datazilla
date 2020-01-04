package com.github.haflife3.test.sqlite

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.dataobject.DummyTableSqlite
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class SqliteTest extends CommonTest{

    @Override
    protected Class<? extends DummyTable> getRecordClass() {
        return DummyTableSqlite.class
    }

    @Override
    protected String getDbType() {
        return DialectConst.SQLITE
    }

    @Override
    protected String tableName() {
        return super.tableName()
    }

    @Override
    protected String cleanUpSql(){
        return "delete from "+tableName()
    }

//    @Ignore
    @Test
    @Override
    void test(){
        super.test()
    }
}
