package com.github.haflife3.test

import com.github.haflife3.datazilla.dialect.DialectConst
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

import javax.sql.DataSource

class CommonInfo {
    static Map<String,String> createTableMap = new HashMap<>()
    static Map<String,ConnInfo> connInfoMap = new HashMap<>()
    static Map<String,String> driverClassNameMap = new HashMap<>()

    static String createMysqlTestTable =
        '''
CREATE TABLE `TABLE_PLACEHOLDER`  ( 
\t`id`         \tbigint(20) AUTO_INCREMENT NOT NULL,
\t`tinyint_f`  \ttinyint(4) NULL,
\t`smallint_f` \tsmallint(6) NULL,
\t`year_f`     \tyear(4) NULL,
\t`int_f`      \tint(11) NULL,
\t`bit_f`      \tbit(1) NULL,
\t`bigint_f`   \tbigint(20) NULL,
\t`float_f`    \tfloat NULL,
\t`double_f`   \tdouble NULL,
\t`decimal_f`  \tdecimal(10,5) NULL,
\t`numeric_f`  \tnumeric(10,5) NULL,
\t`dateTime_f` \tdatetime NULL,
\t`timestamp_f`\ttimestamp NULL,
\t`date_f`     \tdate NULL,
\t`time_f`     \ttime NULL,
\t`char_f`     \tchar(25) NULL,
\t`varchar_f`   \tvarchar(200) NULL,
\t`text_f`     \ttext NULL,
\t`longtext_f` \tlongtext NULL,
\t PRIMARY KEY(id)
\t)

        '''
    static String createH2TestTable =
        '''
CREATE TABLE dummy_table ( 
   id bigint auto_increment PRIMARY KEY NOT NULL, 
   int_f INT , 
   integer_f INTEGER , 
   boolean_f BOOLEAN, 
   bit_f BIT, 
   tinyint_f TINYINT, 
   smallint_f SMALLINT, 
   year_f YEAR, 
   decimal_f DECIMAL(20, 2), 
   double_f DOUBLE, 
   time_f TIME, 
   date_f DATE, 
   timestamp_f TIMESTAMP, 
   datetime_f DATETIME, 
   varchar_f VARCHAR(255), 
   char_f CHAR(10), 
   uuid_f UUID
)
'''

    static {
        createTableMap.put(DialectConst.MYSQL,createMysqlTestTable)
        createTableMap.put(DialectConst.H2,createH2TestTable)
        connInfoMap.put(DialectConst.MYSQL,
            ConnInfo.builder()
                .url("")
                .username("")
                .password("")
                .build()
        )
        connInfoMap.put(DialectConst.H2,
            ConnInfo.builder()
                .url("jdbc:h2:tcp://localhost/~/test")
                .username("sa")
                .build()
        )
        driverClassNameMap.put(DialectConst.MYSQL,"com.mysql.jdbc.Driver")
        driverClassNameMap.put(DialectConst.H2,"org.h2.Driver")
    }

    static DataSource getDataSource(String dbType){
        def connInfo = connInfoMap.get(dbType)
        HikariConfig config = new HikariConfig()
        config.setDriverClassName(driverClassNameMap.get(dbType))
        config.setJdbcUrl(connInfo.url)
        config.setUsername(connInfo.username)
        config.setPassword(connInfo.password)
        HikariDataSource dataSource = new HikariDataSource(config)
        return dataSource
    }
}
