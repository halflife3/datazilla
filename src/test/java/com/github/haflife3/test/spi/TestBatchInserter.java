package com.github.haflife3.test.spi;

import com.github.haflife3.datazilla.dialect.batch.DefaultBatchInserter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestBatchInserter extends DefaultBatchInserter {
    public TestBatchInserter() {
        log.info("== TestBatchInserter ==");
    }
}
