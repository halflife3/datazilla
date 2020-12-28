package com.github.haflife3.test

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.datazilla.QueryEntry
import com.github.haflife3.datazilla.annotation.Column
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.datazilla.logic.TableLoc
import com.github.haflife3.datazilla.logic.TableObjectMetaCache
import com.github.haflife3.datazilla.misc.ExtraParamInjector
import com.github.haflife3.datazilla.misc.GeneralThreadLocal
import com.github.haflife3.datazilla.misc.MiscUtil
import com.github.haflife3.datazilla.pojo.Cond
import com.github.haflife3.datazilla.pojo.Null
import com.github.haflife3.datazilla.pojo.OrderCond
import com.github.haflife3.test.transaction.TransactionTest
import org.apache.commons.dbutils.ResultSetHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.sql.ResultSet
import java.sql.SQLException

class CommonTest {
    private static final Logger logger = LoggerFactory.getLogger(CommonTest.class)

    protected QueryEntry qe

    private List<Class<? extends DummyTable>> getRecordClass(){
        return configMap().get("recordClass") as List<Class<? extends DummyTable>>
    }

    private String getDbType(){
        return configMap().get("dbType")
    }

    private String getSetupDbType(){
        return configMap().get("setupDbType")
    }

    private String tableName(){
        return configMap().get("tableName")
    }

    private String cleanUpSql(){
        return configMap().get("cleanUpSql")
    }

    private String dropTableSql(){
        return configMap().get("dropTableSql")
    }

    private boolean idInt(){
        return configMap().get("idInt")
    }

    private List<String> nullField4Test(){
        return configMap().get("nullField4Test") as List<String>
    }

    protected Map<String,Object> configMap(){
        return [
            "recordClass":[],
            "dbType":"",
            "setupDbType":null,
            "tableName":"dummy_table",
            "cleanUpSql":"truncate table TABLE_PLACEHOLDER",
            "dropTableSql":"drop table TABLE_PLACEHOLDER",
            "idInt":false,
            "nullField4Test":["varcharF","varchar_f"],
        ] as Map<String, Object>
    }

    private static Class<? extends DummyTable> getCurrentClass(){
        return GeneralThreadLocal.get("CurrentClass");
    }

    private static void setCurrentClass(Class<? extends DummyTable> clazz){
        GeneralThreadLocal.set("CurrentClass",clazz)
    }

//    @Before
    void setup(String dbType){
        logger.info('>>setup<<')
        qe = new QueryEntry(CommonInfo.getDataSource(getDbType()),dbType)
        String createTableTemplate = CommonInfo.createTableMap.get(getDbType())
        String createTableSql = createTableTemplate.replace("TABLE_PLACEHOLDER",tableName())
        ExtraParamInjector.sqlId("setup create table")
        qe.genericUpdate(createTableSql)
        String cleanUpSql = cleanUpSql().replace("TABLE_PLACEHOLDER",tableName())
        ExtraParamInjector.sqlId("cleanUpSql")
        qe.genericUpdate(cleanUpSql)
        logger.info '>>setup finish<<'
    }

//    @After
    void cleanup(){
        logger.info '>>cleanup<<'
        String sql = dropTableSql().replace("TABLE_PLACEHOLDER",tableName())
        ExtraParamInjector.sqlId("cleanup drop table")
        qe.genericUpdate(sql)
        GeneralThreadLocal.unset()
        logger.info '>>cleanup finish<<'
    }

    protected void test(){
        logger.info ' -- test -- '
        for(String testDbType in [getSetupDbType(), DialectConst.DEFAULT]){
            getRecordClass().each {
                try {
                    setup(testDbType)
                    condBuild()
                    setCurrentClass(it)
                    logger.info("************ ${getCurrentClass()} *************")
                    testDbType?:dbType()
                    tableMetas()
                    colNames()
                    batchInsert()
                    typeMapping()
                    queryAll()
                    count()
                    extraCondCount()
                    genericQry4Map()
                    querySingleAndExist()
                    selectColumns()
                    order()
                    paging()
                    paging4Map()
                    offset()
                    nameMismatch()
                    extraCondQuery()
                    orCondQuery()
                    moreQuery()
                    insertOne()
                    nullCond()
                    updateSelective()
                    extraCondUpdateSelective()
                    updateSelectiveByFieldOrColumn()
                    updateFull()
                    extraCondUpdateFull()
                    persist()
                    insertAndReturnAutoGen()
                    delOne()
                    extraCondDel()
                    delAll()
                    tx()
                }catch(Exception e){
                    logger.error(e.getMessage(),e)
                    throw e
                } finally {
                    cleanup()
                }
            }
        }
        logger.info ' -- test finish -- '
    }

    void condBuild(){
        logger.info ' -- condBuild -- '
        CondBuildObj obj = new CondBuildObj(
            val1: "v1",
            val2: "v2",
            val3: "v3",
            val4: [1234L,4567L],
            val5: []
        )
        def conds = qe.buildConds(obj)
        assert conds.size() == 4
        assert conds[0].columnName == "val1" && conds[0].compareOpr == "=" && conds[0].value == "v1"
        assert conds[1].columnName == "val_2" && conds[1].compareOpr == "=" && conds[1].value == "v2"
        assert conds[2].columnName == "val3" && conds[2].compareOpr == "like" && conds[2].value == "%v3%"
        assert conds[3].columnName == "val_4" && conds[3].compareOpr == "in" && conds[3].value == [1234L,4567L]
    }

    void dbType(){
        logger.info ' -- dbType -- '
        assert getDbType() == qe.getDbType()
    }

    void tableMetas(){
        logger.info ' -- tableMetas -- '
        def metas = qe.tableMetas
        def tableName = TableLoc.findTableName(getCurrentClass()).toLowerCase()
        assert metas.any {it.key.toLowerCase() == tableName }
    }

    void colNames(){
        logger.info ' -- colNames -- '
        Class<? extends DummyTable> clazz = getCurrentClass()
        ExtraParamInjector.sqlId("colNames")
        List<String> colNames = qe.getColNames(clazz)
        List<String> lowerColNames = new ArrayList<>()
        colNames.each {lowerColNames << it.toLowerCase()}
        Collections.sort(lowerColNames)
        List<String> compareColNames = new ArrayList<>()
        TableObjectMetaCache.initTableObjectMeta(clazz,qe)
        def columnToFieldMap = TableObjectMetaCache.getColumnToFieldMap(clazz)
        columnToFieldMap.each {
            compareColNames << it.key
        }
        Collections.sort(compareColNames)
        lowerColNames.eachWithIndex { String entry, int i ->
            assert entry == compareColNames.get(i)
        }
    }

    void batchInsert(){
        logger.info ' -- batchInsert -- '
        List<? extends DummyTable> list = CommonTool.generateDummyRecords(getCurrentClass(),200)
        ExtraParamInjector.sqlId("batchInsert")
        def insertNum = qe.batchInsert(list)
        assert insertNum == 200
    }

    void typeMapping(){
        logger.info ' -- typeMapping -- '
        ExtraParamInjector.sqlId("typeMapping")
        qe.genericQry("select * from ${tableName()}",new ResultSetHandler<List<Void>>() {
            @Override
            List<Void> handle(ResultSet rs) throws SQLException {
                def metaData = rs.getMetaData()
                def count = metaData.getColumnCount()
                while (rs.next()){
                    for (i in 1 .. count){
                        def object = rs.getObject(i)
                        if(object!=null){
                            logger.info ("${object.getClass().name} # ${metaData.getColumnTypeName(i)} # ${object}")
                        }
                    }
                    break
                }
                return null
            }
        })
    }

    void queryAll(){
        logger.info ' -- queryAll -- '
        ExtraParamInjector.sqlId("queryAll")
        ExtraParamInjector.offset(null,null,false,new OrderCond("id"))
        List<? extends DummyTable> list = qe.searchObjects(getCurrentClass().newInstance())
        assert list.size() == 200
        GeneralThreadLocal.set("allRecords",list)
    }

    void count(){
        logger.info ' -- count -- '
        ExtraParamInjector.sqlId("count step1")
        assert qe.count(getCurrentClass().newInstance()) == 200

        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Query = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        ExtraParamInjector.sqlId("count step2")
        assert qe.count(getCurrentClass(),new Cond("id",id2Query)) == 1
    }

    void extraCondCount(){
        logger.info ' -- extraCondCount -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Query = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        ExtraParamInjector.sqlId("extraCondCount")
        ExtraParamInjector.addCond([new Cond("id",id2Query)])
        assert qe.count(getCurrentClass().newInstance()) == 1
    }

    void genericQry4Map(){
        logger.info ' -- genericQry4Map -- '
        def clazz = getCurrentClass()
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Query = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        String sql = "select * from ${tableName()} where id = ?"
        ExtraParamInjector.sqlId("genericQry4Map step1")
        List<Map<String,Object>> result = qe.genericQry(sql,id2Query)
        assert result.size() == 1
        def search = clazz.newInstance()
        MiscUtil.setValue(search,"id",id2Query)
        ExtraParamInjector.sqlId("genericQry4Map step2")
        def objRecord = qe.searchObject(search)
        def mapRecord = result.get(0)
        def fields = MiscUtil.getAllFields(clazz)

        TableObjectMetaCache.initTableObjectMeta(clazz,qe)
        def fieldToColumnMap = TableObjectMetaCache.getFieldToColumnMap(clazz)
        fields.each {
            String colName = fieldToColumnMap.get(it.getName())
            def column = it.getAnnotation(Column)
            //if customValue presents, the real column should be column.value() or field name
            if(column&&column.customValue()){
                colName = (column?.value())?:it.name
            }
            it.setAccessible(true)
            def value = it.get(objRecord)
            if(value!=null){
                assert mapRecord.get(colName)!=null
            }
        }
    }

    void querySingleAndExist(){
        logger.info ' -- querySingleAndExist -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def record = list.get(0)
        def search = getCurrentClass().newInstance()
        def id = MiscUtil.extractFieldValueFromObj(record,"id")
        search.setId(id)
        ExtraParamInjector.sqlId("querySingleAndExist step1")
        def resultRecord = qe.searchObject(search)
        assert MiscUtil.extractFieldValueFromObj(resultRecord, "id") == id

        ExtraParamInjector.sqlId("querySingleAndExist step2")
        def exist = qe.exist(search)
        assert exist

        ExtraParamInjector.sqlId("querySingleAndExist step3")
        search.setId(-1)
        def notExist = !qe.exist(search)
        assert notExist
    }

    void selectColumns(){
        logger.info ' -- selectColumns -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def record = list.get(0)
        def search = getCurrentClass().newInstance()
        def id = MiscUtil.extractFieldValueFromObj(record,"id")
        MiscUtil.setValue(search,"id",id)
        ExtraParamInjector.selectColumns("id")
        ExtraParamInjector.sqlId("selectColumns")
        def resultRecord = qe.searchObject(search)
        def mapObject = MiscUtil.mapObject(resultRecord)
        mapObject.each {
            if(it.key=="id"){
                assert it.value!=null
            }else {
                assert it.value==null
            }
        }
    }

    void order(){
        logger.info ' -- order -- '
        ExtraParamInjector.order(new OrderCond("id","desc"))
        ExtraParamInjector.sqlId("order")
        List<? extends DummyTable> list = qe.searchObjects(getCurrentClass().newInstance())
        assert list.size() == 200
        def lastId = Integer.MAX_VALUE
        list.each {
            def id = MiscUtil.extractFieldValueFromObj(it,"id")
            assert id < lastId
            lastId = id
        }
    }

    void paging(){
        logger.info ' -- paging -- '
        ExtraParamInjector.paging(1,10,true,new OrderCond("id","desc"))
        ExtraParamInjector.sqlId("paging")
        List<? extends DummyTable> list = qe.searchObjects(getCurrentClass().newInstance())
        def count = ExtraParamInjector.totalCount
        assert list.size() == 10
        assert count == 200
        def lastId = Integer.MAX_VALUE
        list.each {
            def id = MiscUtil.extractFieldValueFromObj(it,"id")
            assert id < lastId
            lastId = id
        }
    }

    void paging4Map(){
        logger.info ' -- paging4Map -- '
        ExtraParamInjector.paging(1,10,true,new OrderCond("id","desc"))
        ExtraParamInjector.sqlId("paging4Map")
        List<Map<String,Object>> list = qe.findObjects(tableName(),[],Map.class)
        def count = ExtraParamInjector.totalCount
        assert list.size() == 10
        assert count == 200
        def lastId = Integer.MAX_VALUE
        list.each {
            def id = it.get('id')
            assert id < lastId
            lastId = id
        }
    }

    void offset(){
        logger.info ' -- offset -- '
        ExtraParamInjector.offset(0,10,false,new OrderCond("id","desc"))
        ExtraParamInjector.sqlId("offset step1")
        List<? extends DummyTable> list = qe.searchObjects(getCurrentClass().newInstance())
        assert list.size() == 10

        ExtraParamInjector.offset(3,5,false,new OrderCond("id","desc"))
        ExtraParamInjector.sqlId("offset step2")
        List<? extends DummyTable> otherList = qe.searchObjects(getCurrentClass().newInstance())
        assert otherList.size() == 5

        def subList = list.subList(3, (5 + 3))
        subList.eachWithIndex { def record, int i ->
            record.getId() == otherList[i].getId()
        }
    }

    void nameMismatch(){
        logger.info ' -- nameMismatch -- '
        ExtraParamInjector.sqlId("nameMismatch step1")
        List<? extends DummyTable> list = qe.searchObjects(getCurrentClass().newInstance())
        list.each {
            assert it.getMismatchedName()!=null
        }
        def search = getCurrentClass().newInstance()
        MiscUtil.setValue(search,"mismatchedName",list[0].getMismatchedName())
        ExtraParamInjector.sqlId("nameMismatch step2")
        def result = qe.searchObject(search)
        assert result!=null
    }

    void extraCondQuery(){
        logger.info ' -- extraCondQuery -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def record = list.get(0)
        def search = getCurrentClass().newInstance()
        def id = MiscUtil.extractFieldValueFromObj(record,"id")
        ExtraParamInjector.addCond([new Cond("id",id)])
        ExtraParamInjector.sqlId("extraCondQuery")
        def result = qe.searchObject(search)
        assert result.getId() == id
    }

    void orCondQuery(){
        logger.info ' -- orCondQuery -- '
        def search = getCurrentClass().newInstance()
        ExtraParamInjector.addOrCond([
            new Cond("id", 100),
            new Cond("id","between", [80,120]),
            new Cond("id","in", [121,122]),
            new Cond("id",new Null())
        ])
        ExtraParamInjector.sqlId("orCondQuery")
        def result = qe.searchObjects(search)
        result.each {
            def id = it.getId()
            assert id>=80&&id<=122
        }
    }

    void moreQuery(){
        logger.info ' -- moreQuery -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        List<? extends DummyTable> list10 = list.subList(0,10)
        List idList10 = new ArrayList()
        list10.each {
            idList10 << it.getId()
        }
        // test in and not in
        List inCondList = new ArrayList()
        Object[] inCondArr = new Object[10]
        list10.eachWithIndex { DummyTable entry, int i ->
            inCondList << entry.getId()
            inCondArr[i] = entry.getId()
        }
        ExtraParamInjector.sqlId("moreQuery inCondList")
        List<? extends DummyTable> inListRt = qe.findObjects(getCurrentClass(),new Cond.Builder().columnName("id").compareOpr("in").value(inCondList).build())
        ExtraParamInjector.sqlId("moreQuery inCondArr")
        List<? extends DummyTable> inArrRt = qe.findObjects(getCurrentClass(),new Cond("id","in",inCondArr))
        ExtraParamInjector.sqlId("moreQuery not inCondList")
        List<? extends DummyTable> notInListRt = qe.findObjects(getCurrentClass(),new Cond.Builder().columnName("id").compareOpr("not in").value(inCondList).build())
        ExtraParamInjector.sqlId("moreQuery not inCondArr")
        List<? extends DummyTable> notInArrRt = qe.findObjects(getCurrentClass(),new Cond("id","not in",inCondArr))
        assert inListRt.size()==10
        assert inArrRt.size()==10
        list10.eachWithIndex { DummyTable entry, int i ->
            assert entry.getId() == inListRt[i].getId()
            assert entry.getId() == inArrRt[i].getId()
        }
        assert notInListRt.every {
            !idList10.contains(it)
        }
        assert notInArrRt.every {
            !idList10.contains(it)
        }

        //test between and not between
        List btCondList = [idList10[0],idList10[9]]
        Object[] btCondArr = new Object[]{idList10[0],idList10[9]}
        ExtraParamInjector.sqlId("moreQuery btCondList")
        List<? extends DummyTable> btListRt = qe.findObjects(getCurrentClass(),new Cond("id","between",btCondList))
        ExtraParamInjector.sqlId("moreQuery btCondArr")
        List<? extends DummyTable> btArrRt = qe.findObjects(getCurrentClass(),new Cond("id","between",btCondList))
        ExtraParamInjector.sqlId("moreQuery not btCondList")
        List<? extends DummyTable> notBtListRt = qe.findObjects(getCurrentClass(),new Cond("id","not between",btCondArr))
        ExtraParamInjector.sqlId("moreQuery not btCondArr")
        List<? extends DummyTable> notBtArrRt = qe.findObjects(getCurrentClass(),new Cond("id","not between",btCondArr))
        assert btListRt.size()==10
        assert btArrRt.size()==10
        list10.eachWithIndex { DummyTable entry, int i ->
            assert entry.getId() == btListRt[i].getId()
            assert entry.getId() == btArrRt[i].getId()
        }
        assert notBtListRt.every {
            !idList10.contains(it)
        }
        assert notBtArrRt.every {
            !idList10.contains(it)
        }
    }

    void insertOne(){
        logger.info ' -- insertOne -- '
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getCurrentClass(), 1))
        ExtraParamInjector.sqlId("insertOne")
        def insertNum = qe.insert(record)
        assert insertNum == 1
    }

    void nullCond(){
        logger.info ' -- nullCond -- '
        def clazz = getCurrentClass()
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(clazz, 1))
        MiscUtil.setValue(record,nullField4Test()[0],null)
        ExtraParamInjector.sqlId("nullCond step1")
        qe.insert(record)
        ExtraParamInjector.offset(0,1,false,new OrderCond("id","desc"))
        ExtraParamInjector.sqlId("nullCond step2")
        def result = qe.findObject(clazz, new Cond(nullField4Test()[1], new Null()))
        def fields = MiscUtil.getAllFields(clazz)
        fields.each {
            it.setAccessible(true)
            def origValue = it.get(record)
            def resultValue = it.get(result)
            if(it.name != "id"){
                compareValueEqual(origValue,resultValue)
            }
        }
    }

    void updateSelective(){
        logger.info ' -- updateSelective -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Update = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        def condObj = getCurrentClass().newInstance()
        condObj.setId(id2Update)
        ExtraParamInjector.sqlId("updateSelective step1")
        def origRecord = qe.searchObject(condObj)
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getCurrentClass(), 1))
        MiscUtil.setValue(record,nullField4Test()[0],null)
        ExtraParamInjector.sqlId("updateSelective step2")
        def updateNum = qe.updateSelectiveAutoCond(record, condObj)
        assert updateNum == 1
        ExtraParamInjector.sqlId("updateSelective step3")
        def updatedRecord = qe.searchObject(condObj)
        assert MiscUtil.extractFieldValueFromObj(origRecord,nullField4Test()[0]) == MiscUtil.extractFieldValueFromObj(updatedRecord,nullField4Test()[0])
    }

    void extraCondUpdateSelective(){
        logger.info ' -- updateSelective -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Update = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        def condObj = getCurrentClass().newInstance()
        condObj.setId(id2Update)
        ExtraParamInjector.sqlId("updateSelective step1")
        def origRecord = qe.searchObject(condObj)
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getCurrentClass(), 1))
        MiscUtil.setValue(record,nullField4Test()[0],null)
        ExtraParamInjector.sqlId("updateSelective step2")
        ExtraParamInjector.addCond([new Cond("id",id2Update)])
        def updateNum = qe.updateSelective(record)
        assert updateNum == 1
        ExtraParamInjector.sqlId("updateSelective step3")
        def updatedRecord = qe.searchObject(condObj)
        assert MiscUtil.extractFieldValueFromObj(origRecord,nullField4Test()[0]) == MiscUtil.extractFieldValueFromObj(updatedRecord,nullField4Test()[0])
    }

    void updateSelectiveByFieldOrColumn(){
        logger.info ' -- updateSelectiveByFieldOrColumn -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        ExtraParamInjector.sqlId("updateSelectiveByFieldOrColumn step1")
        def dbRecord = qe.findObject(getCurrentClass(),new Cond("id",id))
        def condValue = MiscUtil.extractFieldValueFromObj(dbRecord,"mismatchedName")
        def record2Update = getCurrentClass().newInstance()
        MiscUtil.setValue(record2Update,"mismatchedName",condValue)
        MiscUtil.setValue(record2Update,nullField4Test()[0],"valueByField")
        ExtraParamInjector.sqlId("updateSelectiveByFieldOrColumn step2")
        def updateNum = qe.updateSelectiveConcise(record2Update,"mismatchedName")
        assert updateNum == 1
        ExtraParamInjector.sqlId("updateSelectiveByFieldOrColumn step3")
        dbRecord = qe.findObject(getCurrentClass(),new Cond("id",id))
        assert MiscUtil.extractFieldValueFromObj(dbRecord,nullField4Test()[0]) == "valueByField"

        MiscUtil.setValue(record2Update,nullField4Test()[0],"valueByColumn")
        ExtraParamInjector.sqlId("updateSelectiveByFieldOrColumn step4")
        updateNum = qe.updateSelectiveConcise(record2Update,"name_mismatch_f")
        assert updateNum == 1
        ExtraParamInjector.sqlId("updateSelectiveByFieldOrColumn step5")
        dbRecord = qe.findObject(getCurrentClass(),new Cond("id",id))
        assert MiscUtil.extractFieldValueFromObj(dbRecord,nullField4Test()[0]) == "valueByColumn"

    }

    void updateFull(){
        logger.info ' -- updateFull -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Update = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        def condObj = getCurrentClass().newInstance()
        condObj.setId(id2Update)
        ExtraParamInjector.sqlId("updateFull step1")
        def origRecord = qe.searchObject(condObj)
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getCurrentClass(), 1))
        MiscUtil.setValue(record,nullField4Test()[0],null)
        def fieldToColumnMap = TableObjectMetaCache.getFieldToColumnMap(getCurrentClass())
        String idCol = fieldToColumnMap.get('id')
        ExtraParamInjector.sqlId("updateFull step2")
        def updateNum = qe.updateFull(record, condObj,[idCol])
        assert updateNum == 1
        ExtraParamInjector.sqlId("updateFull step3")
        def updatedRecord = qe.searchObject(condObj)
        assert MiscUtil.extractFieldValueFromObj(origRecord,nullField4Test()[0]) != null
        assert MiscUtil.extractFieldValueFromObj(updatedRecord,nullField4Test()[0]) == null
    }

    void extraCondUpdateFull(){
        logger.info ' -- updateFull -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Update = MiscUtil.extractFieldValueFromObj(list.get(1),"id")
        def condObj = getCurrentClass().newInstance()
        condObj.setId(id2Update)
        ExtraParamInjector.sqlId("updateFull step1")
        def origRecord = qe.searchObject(condObj)
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getCurrentClass(), 1))
        MiscUtil.setValue(record,nullField4Test()[0],null)
        def fieldToColumnMap = TableObjectMetaCache.getFieldToColumnMap(getCurrentClass())
        String idCol = fieldToColumnMap.get('id')
        ExtraParamInjector.sqlId("updateFull step2")
        ExtraParamInjector.addCond([new Cond("id",id2Update)])
        def updateNum = qe.updateFull(record, [],[idCol])
        assert updateNum == 1
        ExtraParamInjector.sqlId("updateFull step3")
        def updatedRecord = qe.searchObject(condObj)
        assert MiscUtil.extractFieldValueFromObj(origRecord,nullField4Test()[0]) != null
        assert MiscUtil.extractFieldValueFromObj(updatedRecord,nullField4Test()[0]) == null
    }

    void persist(){
        logger.info ' -- persist -- '
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getCurrentClass(), 1))
        def id = idInt()?1:System.currentTimeMillis()
        record.setId(null)
        def condObj = getCurrentClass().newInstance()
        condObj.setId(id)
        ExtraParamInjector.sqlId("persist")
        def persistNum = qe.persistAutoCond(record, condObj)
        assert persistNum == 1
    }

    void insertAndReturnAutoGen(){
        logger.info ' -- insertAndReturnAutoGen -- '
        def clazz = getCurrentClass()
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(clazz, 1))
        ExtraParamInjector.sqlId("insertAndReturnAutoGen step1")
        def autoGenValue = qe.insertAndReturnAutoGen(record)
        assert autoGenValue!=null
        if(autoGenValue instanceof BigInteger || autoGenValue instanceof BigDecimal){
            autoGenValue = autoGenValue.longValue()
        }
        ExtraParamInjector.sqlId("insertAndReturnAutoGen step2")
        def resultRecord = qe.findObject(clazz, new Cond("id", autoGenValue))
        record.setId(autoGenValue)
        def fields = MiscUtil.getAllFields(clazz)
        fields.each {
            it.setAccessible(true)
            def origValue = it.get(record)
            def resultValue = it.get(resultRecord)
            compareValueEqual(origValue,resultValue)
        }
    }

    void delOne(){
        logger.info ' -- delOne -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Del = MiscUtil.extractFieldValueFromObj(list.get(0),"id")
        def del = getCurrentClass().newInstance()
        MiscUtil.setValue(del,"id",id2Del)
        ExtraParamInjector.sqlId("delOne")
        def delNum = qe.delObjects(del)
        assert delNum == 1
    }

    void extraCondDel(){
        logger.info ' -- extraCondDel -- '
        List<? extends DummyTable> list = GeneralThreadLocal.get("allRecords")
        def id2Del = MiscUtil.extractFieldValueFromObj(list.get(1),"id")
        def del = getCurrentClass().newInstance()
        ExtraParamInjector.sqlId("extraCondDel")
        ExtraParamInjector.addCond([new Cond("id",id2Del)])
        def delNum = qe.delObjects(del)
        assert delNum == 1
    }

    void delAll(){
        logger.info ' -- delAll -- '
        def del = getCurrentClass().newInstance()
        ExtraParamInjector.sqlId("delAll")
        def delNum = qe.delObjects(del)
        assert delNum > 0
    }

    void tx(){
        logger.info ' -- tx -- '

        String createTableTemplate = CommonInfo.createTableMap.get(getDbType())
        String createTableSql = createTableTemplate.replace("TABLE_PLACEHOLDER",tableName())
        Closure clSetup = { QueryEntry queryEntry ->
            ExtraParamInjector.sqlId("tx 000")
            queryEntry.genericUpdate(createTableSql)
        }

        Closure clNormal = { QueryEntry queryEntry ->
            def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getCurrentClass(), 1))
            ExtraParamInjector.sqlId("tx 001")
            queryEntry.insert(record)
        }
        Closure<Boolean> validNormal = { QueryEntry queryEntry ->
            ExtraParamInjector.sqlId("tx 002")
            return queryEntry.searchObjects(getCurrentClass().newInstance()).size() == 1
        }
        assert TransactionTest.tx(getDbType(),clSetup,clNormal,validNormal)
        ExtraParamInjector.sqlId("tx 003")
        qe.delObjects(getCurrentClass().newInstance())

        Closure clException = { QueryEntry queryEntry ->
            def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(getCurrentClass(), 1))
            ExtraParamInjector.sqlId("tx 004")
            queryEntry.insert(record)
            ExtraParamInjector.sqlId("tx 005")
            queryEntry.updateSelective(record,new Cond("field_no_exist","xxx"))
        }
        Closure<Boolean> validException = { QueryEntry queryEntry ->
            ExtraParamInjector.sqlId("tx 006")
            return queryEntry.searchObjects(getCurrentClass().newInstance()).size() == 0
        }
        assert TransactionTest.tx(getDbType(),clSetup,clException,validException)
    }

    static void compareValueEqual(Object origValue, Object resultValue){
        if(origValue!=null){
            if(origValue instanceof Double){
                assert ((Double) origValue).intValue() == ((Double) resultValue).intValue()
            }else if(origValue instanceof Date){
                assert resultValue !=null
            }else if(origValue instanceof String){
                assert ((String)origValue).trim() == ((String)resultValue).trim()
            } else {
                assert origValue == resultValue
            }
        }
    }
}