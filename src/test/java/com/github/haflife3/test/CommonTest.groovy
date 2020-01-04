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
        qe.genericUpdate(createTableSql)
        String truncateSql = "truncate table "+tableName()
        qe.genericUpdate(truncateSql)
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
        persist()
        delOne()
        logger.info ' -- test finish -- '
    }

    void batchInsert(){
        logger.info ' -- batchInsert -- '
        List<? extends DummyTable> list = CommonTool.generateDummyRecords(getRecordClass(),200)
        def insertNum = qe.batchInsert(list)
        assert insertNum == 200
    }

    void queryAll(){
        logger.info ' -- queryAll -- '
        List<? extends DummyTable> list = qe.searchObjects(getRecordClass().newInstance())
        assert list.size() == 200
        GeneralThreadLocal.set("allRecords",list)
    }

    void querySingle(){
        logger.info ' -- querySingle -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def record = list.get(0)
        def search = getRecordClass().newInstance()
        def id = MiscUtil.extractFieldValueFromObj(record,"id")
        MiscUtil.setValue(search,"id",id)
        def resultRecord = qe.searchObject(search)
        assert MiscUtil.extractFieldValueFromObj(resultRecord,"id").equals(id)
    }

    void paging(){
        logger.info ' -- paging -- '
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
        logger.info ' -- insertOne -- '
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getRecordClass(), 1))
        def insertNum = qe.insert(record)
        assert insertNum == 1
    }

    void updateSelective(){
        logger.info ' -- updateSelective -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Update = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getRecordClass(), 1))
        def condObj = getRecordClass().newInstance()
        condObj.setId(id2Update)
        def updateNum = qe.updateSelective(record, condObj)
        assert updateNum == 1

    }

    void persist(){
        logger.info ' -- persist -- '
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getRecordClass(), 1))
        def id = System.currentTimeMillis()
        record.setId(id)
        def condObj = getRecordClass().newInstance()
        condObj.setId(id)
        def persistNum = qe.persist(record, condObj)
        assert persistNum == 1
    }

    void delOne(){
        logger.info ' -- delOne -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Del = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        def del = getRecordClass().newInstance()
        MiscUtil.setValue(del,"id",id2Del)
        def delNum = qe.delObjects(del)
        assert delNum == 1
    }
}
