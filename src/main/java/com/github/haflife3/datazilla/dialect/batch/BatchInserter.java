package com.github.haflife3.datazilla.dialect.batch;



import com.github.haflife3.datazilla.CoreRunner;

import java.util.List;
import java.util.Map;

public interface BatchInserter {
    int batchInsert(CoreRunner coreRunner, String table, List<Map<String, Object>> listMap);
}
