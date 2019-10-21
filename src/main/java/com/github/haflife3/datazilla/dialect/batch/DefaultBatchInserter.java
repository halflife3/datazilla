package com.github.haflife3.datazilla.dialect.batch;

import com.github.haflife3.datazilla.CoreRunner;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;

public class DefaultBatchInserter implements BatchInserter {
    @Override
    public int batchInsert(CoreRunner coreRunner, String table, List<Map<String, Object>> listMap){
        int affected = 0;
        if(CollectionUtils.isNotEmpty(listMap)){
            for (Map<String, Object> map : listMap) {
                affected += coreRunner.insert(table,map);
            }
        }
        return affected;
    }
}
