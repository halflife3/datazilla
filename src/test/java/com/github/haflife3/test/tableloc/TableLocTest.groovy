package com.github.haflife3.test.tableloc

import com.github.haflife3.datazilla.annotation.Table
import com.github.haflife3.datazilla.logic.TableLoc
import org.junit.Test

class TableLocTest {

    @Test
    void test(){
        String packageName = "com.github.haflife3.dataobject"
        String tableName = "dummy_table"
        def classes = TableLoc.findClasses(tableName, packageName)
        assert classes.size() > 0
        classes.each {
            Table table = it.getAnnotation(Table)
            assert table.value().toLowerCase() == tableName
        }
    }
}
