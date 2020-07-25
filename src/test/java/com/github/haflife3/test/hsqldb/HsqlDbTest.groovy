package com.github.haflife3.test.hsqldb


import com.github.haflife3.dataobject.DummyTableHsqlDb
import com.github.haflife3.dataobject.DummyTableHsqlDbAlt
import com.github.haflife3.dataobject.DummyTableHsqlDbGv
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class HsqlDbTest extends CommonTest{

    @Override
    protected Map<String,Object> configMap(){
        def configMap = super.configMap()
        configMap.putAll([
            "recordClass":[DummyTableHsqlDb, DummyTableHsqlDbAlt, DummyTableHsqlDbGv],
            "dbType":DialectConst.HSQLDB,
            "idInt":true,
        ])
        return configMap
    }

    @Test
    @Override
    void test(){
        super.test()
    }
}
