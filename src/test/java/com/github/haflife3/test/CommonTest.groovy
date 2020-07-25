package com.github.haflife3.test

import com.github.haflife3.dataobject.DummyTable
import com.github.haflife3.datazilla.QueryEntry
import com.github.haflife3.datazilla.annotation.Table
import com.github.haflife3.datazilla.annotation.TblField
import com.github.haflife3.datazilla.logic.TableLoc
import com.github.haflife3.datazilla.logic.TableObjectMetaCache
import com.github.haflife3.datazilla.misc.ExtraParamInjector
import com.github.haflife3.datazilla.misc.GeneralThreadLocal
import com.github.haflife3.datazilla.misc.MiscUtil
import com.github.haflife3.datazilla.pojo.Cond
import com.github.haflife3.datazilla.pojo.Null
import com.github.haflife3.datazilla.pojo.OrderCond
import org.apache.commons.dbutils.ResultSetHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.sql.ResultSet
import java.sql.SQLException

class CommonTest {
    private static final Logger logger = LoggerFactory.getLogger(CommonTest.class);

    private QueryEntry qe

    private List<Class<? extends DummyTable>> getRecordClass(){
        return configMap().get("recordClass") as List<Class<? extends DummyTable>>
    }

    private String getDbType(){
        return configMap().get("dbType")
    }

    private String tableName(){
        return configMap().get("tableName")
    }

    private String cleanUpSql(){
        return configMap().get("cleanUpSql")
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
            "tableName":"dummy_table",
            "cleanUpSql":"truncate table dummy_table",
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
    void setup(){
        logger.info('>>setup<<')
        qe = new QueryEntry(CommonInfo.getDataSource(getDbType()))
        String createTableTemplate = CommonInfo.createTableMap.get(getDbType())
        String createTableSql = createTableTemplate.replace("TABLE_PLACEHOLDER",tableName())
        ExtraParamInjector.sqlId("setup create table")
        qe.genericUpdate(createTableSql)
        String cleanUpSql = cleanUpSql()
        ExtraParamInjector.sqlId("cleanUpSql")
        qe.genericUpdate(cleanUpSql)
        logger.info '>>setup finish<<'
    }

//    @After
    void cleanup(){
        logger.info '>>cleanup<<'
        String sql = "drop table "+tableName()
        ExtraParamInjector.sqlId("cleanup drop table")
        qe.genericUpdate(sql)
        GeneralThreadLocal.unset()
        logger.info '>>cleanup finish<<'
    }

    protected void test(){
        logger.info ' -- test -- '
        getRecordClass().each {
            try {
                setup()
                condBuild()
                setCurrentClass(it)
                logger.info("************ ${getCurrentClass()} *************")
                dbType()
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
                paging()
                offset()
                nameMismatch()
                extraCondQuery()
                insertOne()
                nullCond()
                updateSelective()
                extraCondUpdateSelective()
                updateFull()
                extraCondUpdateFull()
                persist()
                insertAndReturnAutoGen()
                delOne()
                extraCondDel()
                delAll()
            } finally {
                cleanup()
            }
        }
        logger.info ' -- test finish -- '
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
        Table table = clazz.getAnnotation(Table)
        boolean autoColumnDetection = table.autoColumnDetection()
        if(autoColumnDetection){
            TableObjectMetaCache.initTableObjectMeta(clazz,qe)
            def columnToFieldMap = TableObjectMetaCache.getColumnToFieldMap(clazz)
            columnToFieldMap.each {
                compareColNames << it.key
            }
        }else {
            def fields = MiscUtil.getAllFields(clazz)
            fields.each {
                if(!it.isSynthetic()){
                    def tblField = it.getAnnotation(TblField)
                    String colName = (tblField.value()?:it.name).toLowerCase()
                    compareColNames << colName
                }
            }
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

        Table table = clazz.getAnnotation(Table)
        boolean autoColumnDetection = table.autoColumnDetection()
        if(autoColumnDetection){
            TableObjectMetaCache.initTableObjectMeta(clazz,qe)
            def fieldToColumnMap = TableObjectMetaCache.getFieldToColumnMap(clazz)
            fields.each {
                String colName = fieldToColumnMap.get(it.getName())
                it.setAccessible(true)
                def value = it.get(objRecord)
                if(value!=null){
                    assert mapRecord.get(colName)!=null
                }
            }
        }else {
            fields.each {
                if(!it.isSynthetic()){
                    def tblField = it.getAnnotation(TblField)
                    String colName = (tblField.value()?:it.name).toLowerCase()
                    it.setAccessible(true)
                    def value = it.get(objRecord)
                    if(value!=null){
                        assert mapRecord.get(colName)!=null
                    }
                }
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
        def updateNum = qe.updateSelectiveAutoCon(record, condObj)
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
        ExtraParamInjector.sqlId("updateFull step2")
        def updateNum = qe.updateFull(record, condObj,["id"])
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
        ExtraParamInjector.sqlId("updateFull step2")
        ExtraParamInjector.addCond([new Cond("id",id2Update)])
        def updateNum = qe.updateFull(record, [],["id"])
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
        record.setId(id)
        def condObj = getCurrentClass().newInstance()
        condObj.setId(id)
        ExtraParamInjector.sqlId("persist")
        def persistNum = qe.persistAutoCon(record, condObj)
        assert persistNum == 1
    }

    void insertAndReturnAutoGen(){
        logger.info ' -- insertAndReturnAutoGen -- '
        def clazz = getCurrentClass()
        def record = MiscUtil.getFirst(CommonTool.generateDummyRecords(clazz, 1))
        ExtraParamInjector.sqlId("insertAndReturnAutoGen step1")
        def autoGenValue = qe.insertAndReturnAutoGen(record)
        assert autoGenValue!=null
        if(autoGenValue instanceof BigInteger){
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