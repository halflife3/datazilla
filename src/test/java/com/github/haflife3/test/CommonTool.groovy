package com.github.haflife3.test

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.dataobject.DummyTableMysql
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

class CommonTool {
    public static void main(String[] args) {

        def records = generateDummyRecords(DummyTableMysql.class, 10)
        records.each {
            println it
        }
    }

    static <T extends DummyTable> List<T> generateDummyRecords(Class<T> cls, int num){
        PodamFactory factory = new PodamFactoryImpl()
        List<T> records = new ArrayList()
        num.times {
            records.add(factory.manufacturePojo(cls))
        }
        return records
    }
}
