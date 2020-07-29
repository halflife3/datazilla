package com.github.haflife3.test

import com.github.haflife3.test.h2.H2Test
import com.github.haflife3.test.hsqldb.HsqlDbTest
import com.github.haflife3.test.mysql.MysqlTest
import com.github.haflife3.test.pg.PgTest
import com.github.haflife3.test.sqlite.SqliteTest
import org.junit.Test

class BundledTest {
    @Test
    void test(){
        for (CommonTest ct in [new MysqlTest(), new PgTest(),new H2Test(),new HsqlDbTest(),new SqliteTest()]){
            ct.test()
        }
    }
}
