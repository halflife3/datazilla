package com.github.haflife3.test.spi

import com.github.haflife3.datazilla.dialect.batch.DefaultBatchInserter
import groovy.util.logging.Slf4j

@Slf4j
class GvTestBatchInserter extends DefaultBatchInserter{
    GvTestBatchInserter() {
        log.info("== GvTestBatchInserter ==")
    }
}
