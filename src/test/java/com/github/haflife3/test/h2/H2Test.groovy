package com.github.haflife3.test.h2

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.dataobject.DummyTableH2
import com.github.haflife3.dataobject.DummyTableH2Alt
import com.github.haflife3.dataobject.DummyTableH2Gv
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class H2Test extends CommonTest{

    @Override
    protected List<Class<? extends DummyTable>> getRecordClass() {
        return [DummyTableH2, DummyTableH2Alt, DummyTableH2Gv]
    }

    @Override
    protected String getDbType() {
        return DialectConst.H2
    }

    @Override
    protected String tableName() {
        return super.tableName()
    }

    @Test
    @Override
    void test(){
        super.test()
    }
}
