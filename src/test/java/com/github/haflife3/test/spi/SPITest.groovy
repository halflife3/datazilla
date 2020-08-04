package com.github.haflife3.test.spi

import com.github.haflife3.datazilla.dialect.batch.*
import com.github.haflife3.test.fakedb.FakeDbBatchInserter
import org.junit.Test

class SPITest {
    @Test
    void test(){
        List<BatchInserter> batchInserterList = new ArrayList<>()
        ServiceLoader<BatchInserter> batchInserters = ServiceLoader.load(BatchInserter.class)
        for (BatchInserter batchInserter : batchInserters) {
            batchInserterList.add(batchInserter)
        }
        assert batchInserterList.size() == 9
        assert batchInserterList[0] instanceof TestBatchInserter
        assert batchInserterList[1] instanceof GvTestBatchInserter
        assert batchInserterList[2] instanceof FakeDbBatchInserter
        assert batchInserterList[3] instanceof DefaultBatchInserter
        assert batchInserterList[4] instanceof HsqlDbBatchInserter
        assert batchInserterList[5] instanceof MsSqlBatchInserter
        assert batchInserterList[6] instanceof MysqlBatchInserter
        assert batchInserterList[7] instanceof PgBatchInserter
        assert batchInserterList[8] instanceof SqliteBatchInserter
    }
}
