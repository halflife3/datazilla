package com.github.haflife3.test.h2


import com.github.haflife3.dataobject.DummyTableH2
import com.github.haflife3.dataobject.DummyTableH2Alt
import com.github.haflife3.dataobject.DummyTableH2Gv
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class H2Test extends CommonTest{

    @Override
    protected Map<String,Object> configMap(){
        def configMap = super.configMap()
        configMap.putAll([
            "recordClass":[DummyTableH2, DummyTableH2Alt, DummyTableH2Gv],
            "dbType":DialectConst.H2,
        ])
        return configMap
    }

    @Test
    @Override
    void test(){
        super.test()
    }
}
