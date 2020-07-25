package com.github.haflife3.test.sqlite


import com.github.haflife3.dataobject.DummyTableSqlite
import com.github.haflife3.dataobject.DummyTableSqliteAlt
import com.github.haflife3.dataobject.DummyTableSqliteGv
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class SqliteTest extends CommonTest{

    @Override
    protected Map<String,Object> configMap(){
        def configMap = super.configMap()
        configMap.putAll([
            "recordClass":[DummyTableSqlite, DummyTableSqliteAlt, DummyTableSqliteGv],
            "dbType":DialectConst.SQLITE,
            "cleanUpSql":"delete from dummy_table",
            "nullField4Test":["textF","text_f"],
        ])
        return configMap
    }

//    @Ignore
    @Test
    @Override
    void test(){
        super.test()
    }
}
