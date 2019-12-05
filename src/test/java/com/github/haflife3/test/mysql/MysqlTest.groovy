package com.github.haflife3.test.mysql

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.dataobject.DummyTableMysql
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class MysqlTest extends CommonTest{

    @Override
    protected Class<? extends DummyTable> getRecordClass() {
        return DummyTableMysql.class
    }

    @Override
    protected String getDbType() {
        return DialectConst.MYSQL
    }

    @Override
    protected String tableName() {
        return super.tableName()
    }

//    @Ignore
    @Test
    @Override
    void test(){
        super.test()
    }
}
