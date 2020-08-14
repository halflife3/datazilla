package com.github.haflife3.datazilla.dialect.batch;

import com.github.haflife3.datazilla.CoreRunner;
import com.github.haflife3.datazilla.dialect.DialectConst;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HsqlDbBatchInserter implements BatchInserter{
    @Override
    public String getDatabaseType() {
        return DialectConst.HSQLDB;
    }

    @Override
    public int batchInsert(CoreRunner coreRunner, String table, List<Map<String, Object>> listMap) {
        int affectedNum = 0;
        if(CollectionUtils.isNotEmpty(listMap)){
            List<String> fields = new ArrayList<>(listMap.get(0).keySet());
            int fieldNum = fields.size();
            StringBuilder sql = new StringBuilder("insert into " + table + " (");
            StringBuilder valueSql = new StringBuilder(" values");
            List<Object> values = new ArrayList<>();
            for (String field : fields) {
                sql.append(field).append(",");
            }
            sql = new StringBuilder(StringUtils.stripEnd(sql.toString(), ",") + ") ");
            for (Map<String, Object> map : listMap) {
                valueSql.append(" ( ");
                for(int i=0;i<fieldNum;i++){
                    String field = fields.get(i);
                    Object value = map.get(field);
                    valueSql.append("?,");
                    values.add(value);
                }
                valueSql = new StringBuilder(StringUtils.stripEnd(valueSql.toString(), ",") + ") ,");
            }
            valueSql = new StringBuilder(StringUtils.stripEnd(valueSql.toString(), ","));
            sql.append(valueSql);
            affectedNum = coreRunner.genericUpdate(sql.toString(),values.toArray());
        }
        return affectedNum;
    }
}
