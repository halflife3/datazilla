package com.github.haflife3.test.table2java

import com.github.haflife3.datazilla.QueryEntry
import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.datazilla.logic.Table2Java
import com.github.haflife3.test.CommonInfo
import groovy.util.logging.Slf4j
import org.apache.commons.io.FileUtils
import org.junit.Test

@Slf4j
class Table2JavaTest {

    Map<String,String> dropTableSqlMap = [
        (DialectConst.MSSQL):"""IF OBJECT_ID(N'dbo.TABLE_PLACEHOLDER', N'U') IS NOT NULL
BEGIN
    drop table TABLE_PLACEHOLDER
END""",
        (DialectConst.H2):"drop table TABLE_PLACEHOLDER IF EXISTS",
        (DialectConst.SQLITE):"drop table IF EXISTS TABLE_PLACEHOLDER"
    ]

    @Test
    void test(){
        testDialect(DialectConst.MYSQL)
        testDialect(DialectConst.PG)
        testDialect(DialectConst.MSSQL)
        testDialect(DialectConst.H2)
        testDialect(DialectConst.HSQLDB)
        testDialect(DialectConst.SQLITE)
    }

    void testDialect(String dialect){
        try {
            def meta = ValidationBank.getMeta(dialect)
            Table2Java.generateByMeta(meta)
            String genJavaFileContent = FileUtils.readFileToString(new File(getJavaFilePath(dialect)),"utf-8")
            assert ValidationBank.javaFileContentMap.get(dialect) == genJavaFileContent
        } finally {
            cleanup(dialect)
        }
    }

    static String getJavaFilePath(String dialect){
        String javaFileName = "TestGen"+dialect
        def meta = ValidationBank.getMeta(dialect)
        String domainPackageDir = meta.getDomainPackage().replaceAll("\\.","/")
        return new File("./").getAbsolutePath()+meta.getSrcRoot() +"/"+domainPackageDir+"/"+javaFileName+".java"
    }

    void cleanup(String dialect){
        String tableName = "test_gen_"+dialect.toLowerCase()
        String javaFilePath = getJavaFilePath(dialect)
        try {
            FileUtils.forceDelete(new File(javaFilePath))
        } catch (Exception e) {
            log.error(e.getMessage(),e)
        }

        String dropTableSql = (dropTableSqlMap.get(dialect)?:"drop table TABLE_PLACEHOLDER").replace("TABLE_PLACEHOLDER",tableName)
        try {
            new QueryEntry(CommonInfo.getDataSource(dialect)).genericUpdate(dropTableSql)
        } catch (Exception e) {
            log.error(e.getMessage(),e)
        }
    }
}
