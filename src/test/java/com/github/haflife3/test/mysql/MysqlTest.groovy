package com.github.haflife3.test.mysql


import com.github.haflife3.dataobject.DummyTableMysql
import com.github.haflife3.dataobject.DummyTableMysqlAlt
import com.github.haflife3.dataobject.DummyTableMysqlGv
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class MysqlTest extends CommonTest{

    @Override
    protected Map<String,Object> configMap(){
        def configMap = super.configMap()
        configMap.putAll([
            "recordClass":[DummyTableMysql, DummyTableMysqlAlt, DummyTableMysqlGv],
            "dbType":DialectConst.MYSQL,
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
