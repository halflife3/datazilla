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
CREATE TABLE IF NOT EXISTS `TABLE_PLACEHOLDER`  ( 
   `id`            bigint(20) AUTO_INCREMENT NOT NULL,
   `tinyint_f`     tinyint(4) NULL,
   `smallint_f`    smallint(6) NULL,
   `year_f`        year(4) NULL,
   `int_f`         int(11) NULL,
   `bit_f`         bit(1) NULL,
   `bigint_f`      bigint(20) NULL,
   `float_f`       float NULL,
   `double_f`      double NULL,
   `decimal_f`     decimal(10,5) NULL,
   `numeric_f`     numeric(10,5) NULL,
   `dateTime_f`    datetime NULL,
   `timestamp_f`   timestamp NULL,
   `date_f`        date NULL,
   `time_f`        time NULL,
   `char_f`        char(25) NULL,
   `varchar_f`     varchar(200) NULL,
   `text_f`        text NULL,
   `longtext_f`    longtext NULL,
    PRIMARY KEY(id)
   )
        '''
    static String createH2TestTable =
        '''
CREATE TABLE IF NOT EXISTS TABLE_PLACEHOLDER ( 
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
    
    static String createPgTestTable =
        '''
CREATE TABLE IF NOT EXISTS "TABLE_PLACEHOLDER"  ( 
   "id"                   bigserial NOT NULL,
   "bigint_f"             bigint NULL,
   "smallint_f"           smallint NULL,
   "integer_f"            integer NULL,
   "decimal_f"            decimal(15,5) NULL,
   "numeric_f"            numeric(15,5) NULL,
   "real_f"               real NULL,
   "double_precision_f"   double precision NULL,
   "float8_f"             float8 NULL,
   "varchar_f"            varchar(200) NULL,
   "character_f"          character(25) NULL,
   "char_f"               char(25) NULL,
   "text_f"               text NULL,
   "timestamp_f"          timestamp NULL,
   "date_f"               date NULL,
   "time_f"               time NULL,
   "boolean_f"            boolean NULL ,
   PRIMARY KEY("id") 
   )
'''

    static {
        createTableMap.put(DialectConst.MYSQL,createMysqlTestTable)
        createTableMap.put(DialectConst.H2,createH2TestTable)
        createTableMap.put(DialectConst.PG,createPgTestTable)
        connInfoMap.put(DialectConst.MYSQL,
            ConnInfo.builder()
                .url("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8")
                .username("root")
                .password("")
                .build()
        )
        connInfoMap.put(DialectConst.H2,
            ConnInfo.builder()
                .url("jdbc:h2:tcp://localhost/~/test")
                .username("sa")
                .build()
        )
        connInfoMap.put(DialectConst.PG,
            ConnInfo.builder()
                .url("")
                .username("")
                .password("")
                .build()
        )
        driverClassNameMap.put(DialectConst.MYSQL,"com.mysql.jdbc.Driver")
        driverClassNameMap.put(DialectConst.H2,"org.h2.Driver")
        driverClassNameMap.put(DialectConst.PG,"org.postgresql.Driver")
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
