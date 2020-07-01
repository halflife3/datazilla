package com.github.haflife3.test.mysql

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.dataobject.DummyTableMysql
import com.github.haflife3.dataobject.DummyTableMysqlAlt
import com.github.haflife3.dataobject.DummyTableMysqlGv
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class MysqlTest extends CommonTest{

    @Override
    protected List<Class<? extends DummyTable>> getRecordClass() {
        return [DummyTableMysql, DummyTableMysqlAlt, DummyTableMysqlGv]
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
