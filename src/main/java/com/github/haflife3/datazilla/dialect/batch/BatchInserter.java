package com.github.haflife3.datazilla.dialect.batch;



import com.github.haflife3.datazilla.CoreRunner;
import com.github.haflife3.datazilla.dialect.DatabaseTypeMatcher;

import java.util.List;
import java.util.Map;

public interface BatchInserter extends DatabaseTypeMatcher {
    int batchInsert(CoreRunner coreRunner, String table, List<Map<String, Object>> listMap);
}
