package com.github.haflife3.test.pg


import com.github.haflife3.dataobject.DummyTablePG
import com.github.haflife3.dataobject.DummyTablePGAlt
import com.github.haflife3.dataobject.DummyTablePGGv
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class PgTest extends CommonTest{

    @Override
    protected Map<String,Object> configMap(){
        def configMap = super.configMap()
        configMap.putAll([
            "recordClass":[DummyTablePG, DummyTablePGAlt, DummyTablePGGv],
            "dbType":DialectConst.PG,
        ])
        return configMap
    }

    @Test
    @Override
    void test(){
        super.test()
    }
}
