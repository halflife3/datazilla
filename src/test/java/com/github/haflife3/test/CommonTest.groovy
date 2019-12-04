package com.github.haflife3.test

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.datazilla.QueryEntry
import com.github.haflife3.datazilla.misc.GeneralThreadLocal
import com.github.haflife3.datazilla.misc.MiscUtil
import com.github.haflife3.datazilla.misc.PagingInjector
import com.github.haflife3.datazilla.pojo.Cond
import com.github.haflife3.datazilla.pojo.OrderCond
import org.junit.After
import org.junit.Before
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CommonTest {
    private static final Logger logger = LoggerFactory.getLogger(CommonTest.class);

    protected QueryEntry qe

    protected Class<? extends DummyTable> getRecordClass(){
        return null
    }

    protected String getDbType(){
        return ''
    }

    protected String tableName(){
        return 'dummy_table'
    }

    @Before
    void setup(){
        logger.info('>>setup<<')
        qe = new QueryEntry(CommonInfo.getDataSource(getDbType()))
        String createTableTemplate = CommonInfo.createTableMap.get(getDbType())
        String createTableSql = createTableTemplate.replace("TABLE_PLACEHOLDER",tableName())
        String cleanupSql = "DROP TABLE IF EXISTS "+tableName()
        qe.genericUpdate(cleanupSql)
        qe.genericUpdate(createTableSql)
        logger.info '>>setup finish<<'
    }

    @After
    void cleanup(){
        logger.info '>>cleanup<<'
        String sql = "drop table "+tableName()
        qe.genericUpdate(sql)
        GeneralThreadLocal.unset()
        logger.info '>>cleanup finish<<'
    }

    protected void test(){
        logger.info ' -- test -- '
        batchInsert()
        queryAll()
        querySingle()
        paging()
        insertOne()
        updateSelective()
        delOne()
        logger.info ' -- test finish -- '
    }

    void batchInsert(){
        List<? extends DummyTable> list = CommonTool.generateDummyRecords(getRecordClass(),200)
        def insertNum = qe.batchInsert(list)
        assert insertNum == 200
    }

    void queryAll(){
        List<? extends DummyTable> list = qe.searchObjects(getRecordClass().newInstance())
        assert list.size() == 200
        GeneralThreadLocal.set("allRecords",list)
    }

    void querySingle(){
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def record = list.get(0)
        def search = getRecordClass().newInstance()
        def id = MiscUtil.extractFieldValueFromObj(record,"id")
        MiscUtil.setValue(search,"id",id)
        def resultRecord = qe.searchObject(search)
        assert MiscUtil.extractFieldValueFromObj(resultRecord,"id").equals(id)
    }

    void paging(){
        PagingInjector.fillParam(1,10,true,new OrderCond("id","desc"))
        List<? extends DummyTable> list = qe.searchObjects(getRecordClass().newInstance())
        def count = PagingInjector.count
        assert list.size() == 10
        assert count == 200
        def lastId = Integer.MAX_VALUE
        list.each {
            def id = MiscUtil.extractFieldValueFromObj(it,"id")
            assert id < lastId
            lastId = id
        }
    }

    void insertOne(){
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getRecordClass(), 1))
        def insertNum = qe.insert(record)
        assert insertNum == 1
    }

    void updateSelective(){
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Update = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getRecordClass(), 1))
        def updateNum = qe.updateSelective(record, new Cond("id", id2Update))
        assert updateNum == 1

    }

    void delOne(){
        def del = getRecordClass().newInstance()
        MiscUtil.setValue(del,"id",1L)
        def delNum = qe.delObjects(del)
        assert delNum == 1
    }
}
