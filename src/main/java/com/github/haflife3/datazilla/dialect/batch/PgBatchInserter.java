package com.github.haflife3.datazilla.dialect.batch;

import com.github.haflife3.datazilla.CoreRunner;
import com.github.haflife3.datazilla.dialect.DialectConst;
import com.github.haflife3.datazilla.dialect.DialectFactory;
import com.github.haflife3.datazilla.dialect.regulate.EntityRegulator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PgBatchInserter implements BatchInserter {
    @Override
    public String getDatabaseType() {
        return DialectConst.PG;
    }
    @Override
    public int batchInsert(CoreRunner coreRunner, String table, List<Map<String, Object>> listMap){
        int affectedNum = 0;
        if(CollectionUtils.isNotEmpty(listMap)){
            EntityRegulator entityRegulator = DialectFactory.getEntityRegulator(coreRunner.getDbType());
            List<String> fields = new ArrayList<>(listMap.get(0).keySet());
            int fieldNum = fields.size();
            String sql = "insert into "+table+" (";
            String valueSql = " values";
            List<Object> values = new ArrayList<>();
            for (String field : fields) {
                sql += field + ",";
            }
            sql = StringUtils.stripEnd(sql,",")+") ";
            for (Map<String, Object> map : listMap) {
                valueSql+=" ( ";
                for(int i=0;i<fieldNum;i++){
                    String field = fields.get(i);
                    Object value = map.get(field);
                    valueSql += "?,";
                    values.add(value);
                }
                valueSql = StringUtils.stripEnd(valueSql,",")+") ,";
            }
            valueSql = StringUtils.stripEnd(valueSql,",");
            sql = sql+valueSql;
            affectedNum = coreRunner.genericUpdate(sql,values.toArray());
        }
        return affectedNum;
    }
}
