package com.github.haflife3.test.mssql

import com.github.haflife3.dataobject.DummyTableMsSql
import com.github.haflife3.dataobject.DummyTableMsSqlAlt
import com.github.haflife3.dataobject.DummyTableMsSqlGv
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.CommonTest
import org.junit.Test

class MsSqlTest extends CommonTest{

    @Override
    protected Map<String,Object> configMap(){
        def configMap = super.configMap()
        configMap.putAll([
            "recordClass":[DummyTableMsSql, DummyTableMsSqlAlt, DummyTableMsSqlGv],
            "dropTableSql":"""
IF OBJECT_ID(N'dbo.TABLE_PLACEHOLDER', N'U') IS NOT NULL
BEGIN
    drop table TABLE_PLACEHOLDER
END
""",
            "dbType":DialectConst.MSSQL,
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
