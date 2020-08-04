package com.github.haflife3.test

import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.test.fakedb.FakeDBTest
import com.github.haflife3.test.h2.H2Test
import com.github.haflife3.test.hsqldb.HsqlDbTest
import com.github.haflife3.test.mssql.MsSqlTest
import com.github.haflife3.test.mysql.MysqlTest
import com.github.haflife3.test.pg.PgTest
import com.github.haflife3.test.spi.SPITest
import com.github.haflife3.test.sqlite.SqliteTest
import com.github.haflife3.test.table2java.Table2JavaTest
import com.github.haflife3.test.tableloc.TableLocTest
import org.junit.Test

class BundledTest {
    @Test
    void test(){
        for (CommonTest ct in [
            new MysqlTest(),
            new PgTest(),
            new MsSqlTest(),
            new H2Test(),
            new HsqlDbTest(),
            new SqliteTest(),
            new FakeDBTest()
        ]){
            ct.test()
        }
        for (String dialect in [
            DialectConst.MYSQL,
            DialectConst.PG,
            DialectConst.MSSQL,
            DialectConst.H2,
            DialectConst.HSQLDB,
            DialectConst.SQLITE,
        ]){
            new Table2JavaTest().testDialect(dialect)
        }
        new SPITest().test()
        new TableLocTest().test()
    }
}
