package com.github.haflife3.test.spi

import com.github.haflife3.datazilla.dialect.batch.*
import org.junit.Test

class SPITest {
    @Test
    void test(){
        List<BatchInserter> batchInserterList = new ArrayList<>()
        ServiceLoader<BatchInserter> batchInserters = ServiceLoader.load(BatchInserter.class)
        for (BatchInserter batchInserter : batchInserters) {
            batchInserterList.add(batchInserter)
        }
        assert batchInserterList.size() == 8
        assert batchInserterList[0] instanceof TestBatchInserter
        assert batchInserterList[1] instanceof GvTestBatchInserter
        assert batchInserterList[2] instanceof DefaultBatchInserter
        assert batchInserterList[3] instanceof HsqlDbBatchInserter
        assert batchInserterList[4] instanceof MsSqlBatchInserter
        assert batchInserterList[5] instanceof MysqlBatchInserter
        assert batchInserterList[6] instanceof PgBatchInserter
        assert batchInserterList[7] instanceof SqliteBatchInserter
    }
}
