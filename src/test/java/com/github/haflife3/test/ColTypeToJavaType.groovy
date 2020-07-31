package com.github.haflife3.test

import com.github.haflife3.datazilla.QueryEntry
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.commons.dbutils.ResultSetHandler

import javax.sql.DataSource
import java.sql.ResultSet
import java.sql.SQLException

static DataSource getDataSource(String dbType){
    def connInfo = ConnInfo.builder()
            .url("jdbc:sqlserver://127.0.0.1:2433;database=test")
            .username("sa")
            .password("")
            .build()
    HikariConfig config = new HikariConfig()
    config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
    config.setJdbcUrl(connInfo.url)
    config.setUsername(connInfo.username)
    config.setPassword(connInfo.password)
    HikariDataSource dataSource = new HikariDataSource(config)
    return dataSource
}

QueryEntry queryEntry = new QueryEntry(getDataSource())
queryEntry.genericQry("select * from dummy where 1=2",new ResultSetHandler<List<Void>>() {
    @Override
    List<Void> handle(ResultSet rs) throws SQLException {
        def metaData = rs.getMetaData()
        def count = metaData.getColumnCount()
        for (i in 1 .. count){
            println ("${metaData.getColumnTypeName(i)} # ${metaData.getColumnClassName(i)}")
        }
        while (rs.next()){
            for (i in 1 .. count){
                def object = rs.getObject(i)
                if(object!=null){
                    println ("${object.getClass().name} # ${metaData.getColumnTypeName(i)} # ${object}")
                }
            }
            break
        }
        return null
    }
})