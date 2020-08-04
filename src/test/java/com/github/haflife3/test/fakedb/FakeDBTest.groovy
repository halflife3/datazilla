package com.github.haflife3.test.fakedb

import com.github.haflife3.dataobject.DummyTableHsqlDb
import com.github.haflife3.dataobject.DummyTableHsqlDbAlt
import com.github.haflife3.dataobject.DummyTableHsqlDbGv
import com.github.haflife3.test.CommonTest
import org.junit.Test

class FakeDBTest extends CommonTest{

    @Override
    protected Map<String,Object> configMap(){
        def configMap = super.configMap()
        configMap.putAll([
            "recordClass":[DummyTableHsqlDb, DummyTableHsqlDbAlt, DummyTableHsqlDbGv],
            "dbType": FakeDBConst.DB_TYPE,
            "setupDbType": FakeDBConst.DB_TYPE,
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
