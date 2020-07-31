package com.github.haflife3.test

import com.github.haflife3.datazilla.dialect.DialectConst
import com.google.gson.Gson
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
   `id`                 bigint(20) AUTO_INCREMENT NOT NULL,
   `tinyint_f`          tinyint(4) NULL,
   `smallint_f`         smallint(6) NULL,
   `year_f`             year(4) NULL,
   `int_f`              int(11) NULL,
   `bit_f`              bit(1) NULL,
   `bigint_f`           bigint(20) NULL,
   `float_f`            float NULL,
   `double_f`           double NULL,
   `decimal_f`          decimal(10,5) NULL,
   `numeric_f`          numeric(10,5) NULL,
   `dateTime_f`         datetime NULL,
   `timestamp_f`        timestamp NULL,
   `date_f`             date NULL,
   `time_f`             time NULL,
   `char_f`             char(25) NULL,
   `varchar_f`          varchar(200) NULL,
   `name_mismatch_f`    varchar(200) NULL,
   `text_f`             text NULL,
   `longtext_f`         longtext NULL,
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
   name_mismatch_f VARCHAR(255), 
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
   "name_mismatch_f"      varchar(200) NULL,
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

    static String createSqliteTestTable =
            '''
CREATE TABLE IF NOT EXISTS TABLE_PLACEHOLDER ( 
   id INTEGER PRIMARY KEY NOT NULL, 
   text_f TEXT , 
   name_mismatch_f TEXT , 
   real_f REAL , 
   numeric_f NUMERIC
)
'''

    static String createHsqlDbTestTable =
        '''
CREATE TABLE IF NOT EXISTS TABLE_PLACEHOLDER  ( 
   id            IDENTITY NOT NULL,
   tinyint_f     tinyint NULL,
   smallint_f    smallint NULL,
   integer_f        INTEGER NULL,
   BIGINT_f         BIGINT NULL,
   REAL_f         REAL NULL,
   FLOAT_f      FLOAT  NULL,
   double_f      double NULL,
   decimal_f     decimal(10,5) NULL,
   numeric_f     numeric(10,5) NULL,
   BOOLEAN_f     BOOLEAN NULL,
   dateTime_f    datetime NULL,
   timestamp_f   timestamp NULL,
   date_f        date NULL,
   time_f        time NULL,
   char_f        char(25) NULL,
   varchar_f     varchar(200) NULL,
   name_mismatch_f     varchar(200) NULL,
   LONGVARCHAR_f        LONGVARCHAR NULL,
   UUID_f    UUID NULL,
    PRIMARY KEY(id)
)
'''

    static String createMsSqlTestTable =
        '''
IF OBJECT_ID(N'dbo.TABLE_PLACEHOLDER', N'U') IS  NULL
BEGIN
    CREATE TABLE TABLE_PLACEHOLDER  ( 
        id                 bigint IDENTITY NOT NULL,
        bit_f              bit NULL,
        char_f             char(25) NULL,
        date_f             date NULL,
        datetime_f         datetime NULL,
        decimal_f          decimal(15,5) NULL,
        float_f            float NULL,
        int_f              int NULL,
        nchar_f            nchar(25) NULL,
        ntext_f            ntext NULL,
        numeric_f          numeric(15,5) NULL,
        nvarchar_f         nvarchar(25) NULL,
        real_f             real NULL,
        smalldatetime_f    smalldatetime NULL,
        smallint_f         smallint NULL,
        text_f             text NULL,
        tinyint_f          tinyint NULL,
        varchar_f          varchar(25) NULL,
        name_mismatch_f    varchar(25) NULL,
        CONSTRAINT TABLE_PLACEHOLDER_pk PRIMARY KEY CLUSTERED(id)
    )
END
'''

    static {
        createTableMap.put(DialectConst.MYSQL,createMysqlTestTable)
        createTableMap.put(DialectConst.H2,createH2TestTable)
        createTableMap.put(DialectConst.PG,createPgTestTable)
        createTableMap.put(DialectConst.SQLITE,createSqliteTestTable)
        createTableMap.put(DialectConst.HSQLDB,createHsqlDbTestTable)
        createTableMap.put(DialectConst.MSSQL,createMsSqlTestTable)
        String mysqlConn = System.getenv("DATAZILLA_MYSQL_CONN")
        connInfoMap.put(DialectConst.MYSQL,
            mysqlConn?new Gson().fromJson(mysqlConn,ConnInfo):
            ConnInfo.builder()
                .url("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC")
                .username("root")
                .password("")
                .build()
        )
        connInfoMap.put(DialectConst.H2,
            ConnInfo.builder()
                .url("jdbc:h2:mem:")
                .username("sa")
                .build()
        )
        String pgConn = System.getenv("DATAZILLA_PG_CONN")
        connInfoMap.put(DialectConst.PG,
            pgConn?new Gson().fromJson(pgConn,ConnInfo):
            ConnInfo.builder()
                .url("jdbc:postgresql://localhost:5432/postgres")
                .username("postgres")
                .password("root")
                .build()
        )
        connInfoMap.put(DialectConst.SQLITE,
                ConnInfo.builder()
                        .url("jdbc:sqlite::memory:")
                        .build()
        )
        connInfoMap.put(DialectConst.HSQLDB,
            ConnInfo.builder()
                .url("jdbc:hsqldb:mem:test")
                .username("SA")
                .build()
        )
        String mssqlConn = System.getenv("DATAZILLA_MSSQL_CONN")
        connInfoMap.put(DialectConst.MSSQL,
            mssqlConn?new Gson().fromJson(mssqlConn,ConnInfo):
            ConnInfo.builder()
                .url("jdbc:sqlserver://127.0.0.1:2433;database=test")
                .username("sa")
                .password("")
                .build()
        )
        driverClassNameMap.put(DialectConst.MYSQL,"com.mysql.jdbc.Driver")
        driverClassNameMap.put(DialectConst.H2,"org.h2.Driver")
        driverClassNameMap.put(DialectConst.PG,"org.postgresql.Driver")
        driverClassNameMap.put(DialectConst.SQLITE,"org.sqlite.JDBC")
        driverClassNameMap.put(DialectConst.HSQLDB,"org.hsqldb.jdbc.JDBCDriver")
        driverClassNameMap.put(DialectConst.MSSQL,"com.microsoft.sqlserver.jdbc.SQLServerDriver")
    }

    static DataSource getDataSource(String dbType){
        def connInfo = connInfoMap.get(dbType)
        HikariConfig config = new HikariConfig()
        config.setDriverClassName(driverClassNameMap.get(dbType))
        config.setJdbcUrl(connInfo.url)
        config.setMaximumPoolSize(5)
        config.setUsername(connInfo.username)
        config.setPassword(connInfo.password)
        HikariDataSource dataSource = new HikariDataSource(config)
        return dataSource
    }
}
