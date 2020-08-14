# datazilla
datazilla is a Java library for relational database access. 
It is based on and inspired by [Apache Commons DbUtils](https://commons.apache.org/proper/commons-dbutils/).

As yet another ORM framework, you can use it to interact with databases in a way you would with other Java APIs, and in most cases you don't need to worry about the SQL part.

It comes with a minimum configuration requirement, and a set of user-friendly out of box APIs to help you with simplifying your code regarding database operations.

Requirement: JDK1.8+ .


Specially optimized for MySQL, PostgreSql, SQL Server(2012+), H2, SQLite, HsqlDb.
(paging, batch insert, Java file generation from table definition)

Table of Contents
=================

* [Maven Setting](#maven-setting)
* [Usage](#usage)
  * [Setup](#Setup)
  * [Simple Cases](#Simple-Cases)
    * [insert](#insert)
    * [update](#update)
    * [query](#query)
    * [delete](#delete)
  * [Advanced Cases](#Advanced-Cases)
    * [batch insert](#batch-insert)
    * [persist](#persist)
    * [paging and offset](#paging-and-offset)
    * [SQL identify](#SQL-identify)
    * [column selection](#column-selection)
    * [custom result set handler](#custom-result-set-handler)
    * [auto column detection](#auto-column-detection)
  * [Other functionalities](#Other-functionalities)
    * [Java file generation](#Java-file-generation)
    * [transaction support](#transaction-support)

# Maven Setting
datazilla is available in maven central repo.
```xml
<dependency>
    <groupId>com.github.haflife3</groupId>
    <artifactId>datazilla</artifactId>
    <version>1.2.0</version>
</dependency>
```

# Usage
## Setup
Instantiate QueryEntry with a DataSource as the constructor parameter, and it's all done.

The underlying DB type will be automatically determined, a default one will be applied if none aforementioned can be matched.
```java
DataSource ds = ... // it can be any implementation of javax.sql.DataSource
QueryEntry queryEntry = new QueryEntry(ds);
```

## Simple Cases
First we create a table, and a corresponding Java bean file: [Java file generation](#Java-file-generation)

**`NOTE:`** All SQL snippets here, including DDL, DQL and DML, conform to MySQL syntax, these may vary slightly under other databases, but generally don't affect the validity of those examples listed below.
```sql
CREATE TABLE IF NOT EXISTS `dummy`  ( 
   `id`            bigint(20) AUTO_INCREMENT NOT NULL,
   `int_f`         int(11) NULL,
   `decimal_f`     decimal(10,5) NULL,
   `dateTime_f`    datetime DEFAULT CURRENT_TIMESTAMP,
   `varchar_f`     varchar(200) NULL,
    PRIMARY KEY(id)
)
```
**`NOTE:`** The `implements Serializable` part has no special meaning here, it is only added to conform to the Java Bean Specification, you can safely omit this without having to worry about any side effect. 
```java
@Table("dummy")
public class Dummy implements Serializable {

  @TblField("id")
  private Long id;

  @TblField("int_f")
  private Integer intF;

  @TblField("decimal_f")
  private Double decimalF;

  @TblField("dateTime_f")
  private Date datetimeF;

  @TblField("varchar_f")
  private String varcharF;

  // getter setter omitted...
}
```

### insert
To create a record and save it to database, simply instantiate a Java object and fill it with data, then call `QueryEntry.insert()`. Any null values will be ignored, and the database itself may decide either give them default values or simply left empty. In this case, we left the `id` field unset and let database give it an auto incremented value.
```java
Dummy dummy = new Dummy();
dummy.setIntF(10);
dummy.setDecimalF(12.34);
dummy.setDatetimeF(new Date());
dummy.setVarcharF("some text");
queryEntry.insert(dummy);
```
Corresponding SQL:
```sql
insert into dummy (int_f,decimal_f,dateTime_f,varchar_f) values(?,?,?,?) -- values[10,12.34,<new Date()>,"some text"]
```

### update
To update records by conditions (in this case, by one id), instantiate a Java object and fill it with data you wish to modify, add conditions which will filter out the records in question, then call `QueryEntry.updateSelective()`. Do note this: any null value fields will be ignored for record modification, if however, you wish to include those null value fields, you can use `QueryEntry.updateFull()` instead.

**`TIP:`** It's almost always a bad practice to have primitive typed fields be mapped to database columns, for those fields can never be null and may raise unexpected behaviours.
```java
Dummy dummy = new Dummy();
dummy.setVarcharF("other text");
queryEntry.updateSelective(dummy,new Cond("id",1L));
```
Corresponding SQL:
```sql
update dummy set varchar_f = ? where id = ? -- values["other text",1]
```

### query
There are two set of APIs you can choose to query records from database: (searchObjects,searchObject) and (findObjects,findObject).

The first one is more object oriented, the second one is more flexible.

When using `QueryEntry.searchObjects()` or `QueryEntry.searchObject()`, the object you passed in as the parameter will be transformed as a list of conditions (null value fields ignored), then in the background, this `QueryEntry.findObjects()` method will be invoked.
```java
Dummy dummy = new Dummy();
dummy.setId(1L);
//find all matched records
List<Dummy> dummies = queryEntry.searchObjects(dummy);
//find the first matched record
Dummy dummyFound = queryEntry.searchObject(dummy);
//some more flexible ways to query
dummies = queryEntry.findObjects(Dummy.class, new Cond("id", ">", 0));
dummies = queryEntry.findObjects(Dummy.class, new Cond("id", 1), new Cond("int_f", 10));
dummies = queryEntry.findObjects(Dummy.class, new Cond.Builder().columnName("id").compareOpr("is not null").build());
```
Corresponding SQLs:
```sql
select * from dummy where id = ? -- values[1]
select * from dummy where id = ? -- values[1]
select * from dummy where id > ? -- values[0]
select * from dummy where id = ? and int_f = ? -- values[1,10]
select * from dummy where id is not null
```

### delete
As with [query](#query), there are two ways to delete records.

You can use the object oriented way, that is, object as condition, or the more flexible way as the code below demonstrated.
```java
Dummy dummy = new Dummy();
dummy.setId(1L);
queryEntry.delObjects(dummy);
//a more flexible way to delete
queryEntry.delObjects(Dummy.class, new Cond("id", 1));
```
Corresponding SQL:
```sql
delete from dummy where id = ? -- values[1]
```
**`TIP:`** Actually, there are more ways to implement those basic operations, such as you can specify the table name and a list of conditions to perform a deletion should you wish not to have a table related Java class at all. Even further, you can directly execute SQL queries and deal with result yourself. Those, of course, are not very ORM-ish. Feel free to explore `QueryEntry` and find more.

## Advanced Cases
In this part, the examples are still based on the table and the Java Bean we created in the beginning.

### batch insert
To save multiple records to database at the same time, instantiate one or more Java objects and fill them with data, then call `QueryEntry.batchInsert()`. Null values are ignored as always. You can place records separately into the method call as `Varargs` or altogether as a list.

**`TIP:`** The default bulk size for batch insert is 100, you can specify the size by place an integer number as the first method parameter (e.g: `QueryEntry.batchInsert(200, Arrays.asList(dummy1,dummy2,dummy3 ... ))`).

**`TIP:`** It is recommended to use this inside a transaction scope. [transaction support](#transaction-support)

**`NOTE:`** The single `batchInsert` operation below is separated into two insert queries. datazilla will automatically group together those records which can be operated in a single query. The reason for this seemingly odd behaviour is that we must let database decide how to deal with the null values, `dummy3` here doesn't have `varcharF` set, if there is only one insert query, it would look like this: `insert into dummy (int_f,varchar_f) values (?,?) , ( ?,?), ( ?,?) -- values: [10, "some text", 20, "other text", 30, null]`. Note the last value is null, if `varchar_f` has a default value, the default value can never be applied. 
```java
Dummy dummy1 = new Dummy();
dummy1.setIntF(10);
dummy1.setVarcharF("some text");
Dummy dummy2 = new Dummy();
dummy2.setIntF(20);
dummy2.setVarcharF("other text");
Dummy dummy3 = new Dummy();
dummy3.setIntF(30);
//or queryEntry.batchInsert(Arrays.asList(dummy1,dummy2,dummy3));
queryEntry.batchInsert(dummy1,dummy2,dummy3);
```
Corresponding SQL:
```sql
insert into dummy (int_f,varchar_f) values (?,?) , ( ?,?) -- values: [10, "some text", 20, "other text"]
insert into dummy (int_f) values (?)  -- values: [30]
```

### persist
To persist (same as upsert if you are more familiar with this term) a record into database, instantiate a Java object and fill it with data, specify the conditions to filter out the record to be updated if it ever exists, then call `QueryEntry.persist()`. datazilla will first try to perform an update operation by the conditions, if no database record is updated, the object will be inserted into database instead.

**`TIP:`** It is recommended to use this inside a transaction scope. [transaction support](#transaction-support)
```java
Dummy dummy = new Dummy();
dummy.setIntF(40);
dummy.setVarcharF("some text");
queryEntry.persist(dummy,new Cond("id",10L));
```
Corresponding SQL:
```sql
update dummy set int_f=?,varchar_f=? where id = ?  -- values: [40, "some text", 10]
insert into dummy (int_f,varchar_f)  values( ?,?)   -- values: [40, "some text"]
```

### paging and offset
To query a subset of records filtered by conditions, and optionally get the total count, supply a paging(`ExtraParamInjector.paging()`) or an offset(`ExtraParamInjector.offset()`) instruction right before the query action(`QueryEntry.findObjects()` or `QueryEntry.searchObjects()`). The total count, if required, can be retrieved(`ExtraParamInjector.getTotalCount()`) right after the query action.

**`TIP:`** `ExtraParamInjector.paging()` and `ExtraParamInjector.offset()` should be placed right before the related query action, and `ExtraParamInjector.getTotalCount()` right after it, or else they may be intercepted by other queries by accident and yield undesired result.

**`TIP:`** It is recommended to use this inside a read-only transaction scope. [transaction support](#transaction-support)

**`NOTE:`**  The calculation formula of `pageNo, pageSize` to `offset, limit`: <offset = (pageNo - 1) * pageSize, limit = pageSize> .
```java
//paging by pageNo and pageSize, order by id in descending order, and return total count
ExtraParamInjector.paging(2,5,true,new OrderCond("id","desc"));
List<Dummy> dummies1 = queryEntry.findObjects(Dummy.class, new Cond("id", ">", 0));
//total count
Integer totalCount = ExtraParamInjector.getTotalCount();

//paging by offset and limit, order by id in descending order, and skip total count
ExtraParamInjector.offset(1,2,false,new OrderCond("id","desc"));
List<Dummy> dummies2 = queryEntry.findObjects(Dummy.class,new Cond("id",">",0));
```
Corresponding SQL:
```sql
-- paging by pageNo and pageSize
select * from dummy where id > ? order by id desc limit ?,?  -- values: [0, 5, 5]
select count(*) as count from dummy where id > ?  -- values: [0]

-- paging by offset and limit
select * from dummy where id > ? order by id desc limit ?,?   -- values: [0, 1, 2]
```

### SQL identify
Due to datazilla's nature of constructing SQL at runtime, SQL query can be hard to trace back to, by adding a comment as its identity, tracing will no longer be as hard. 

**`TIP:`** Place `ExtraParamInjector.sqlId()` right before your query action for the same reason as when you are using `ExtraParamInjector.paging()` and `ExtraParamInjector.offset()`.
```java
ExtraParamInjector.sqlId("find the dumbest dummy");
Dummy dummy = queryEntry.findObject(Dummy.class, new Cond("id", 1));
```
Corresponding SQL:
```sql
/* find the dumbest dummy */ select * from dummy where id = ?  -- values: [1]
```

### column selection
To specify which table columns to query instead of all, supply a list of column names in `ExtraParamInjector.selectColumns()` and place it right before the query action.

**`TIP:`** Place `ExtraParamInjector.selectColumns()` right before your query action for the same reason as when you are using `ExtraParamInjector.paging()` and `ExtraParamInjector.offset()`.
```java
ExtraParamInjector.selectColumns("int_f","varchar_f");
Dummy dummy = queryEntry.findObject(Dummy.class, new Cond("id", 1));
```
Corresponding SQL:
```sql
select int_f, varchar_f from dummy where id = ?  -- values: [1]
```

### custom result set handler
To fully control the SQL constructing and result handling yourself, supply a predefined SQL, a `ResultSetHandler` implementation, and a list of values as positional parameters for the SQL(can be skipped if none is required), then call `QueryEntry.genericQry()`.

**`TIP:`** See also `QueryEntry.genericUpdate` for custom update.
```java
List<Dummy> dummies = queryEntry.genericQry("select * from dummy where id > ?",new ResultSetHandler<List<Dummy>>(){
            @Override
            public List<Dummy> handle(ResultSet rs) throws SQLException {
                List<Dummy> dummyList = new ArrayList<>();
                while (rs.next()){
                    int intF = rs.getInt("int_f");
                    Dummy dummy = new Dummy();
                    dummy.setIntF(intF*2);
                    dummyList.add(dummy);
                }
                return dummyList;
            }
        },0L);
```
Corresponding SQL:
```sql
select * from dummy where id > ?  -- values: [0]
```

### auto column detection
If table column names and corresponding Java field names meet a [certain pattern](#match-pattern), they can be automatically paired by adding this configuration(`autoColumnDetection = true`) to `@Table` annotation, and no longer we have to add `@TblField` on each Java field.

**`TIP:`** `@TblField` can still take precedence over `autoColumnDetection`.
#### match pattern:
case-insensitive and underscore ignored

| table column   | Java field   | match    |
| :------------- | :----------: | -------: |
|  varchar_f     | varchar_f    | YES      |
|  varchar_f     | varcharf     | YES      |
|  varchar_f     | varcharF     | YES      |
|  VARCHAR_F     | varcharF     | YES      |
|  varchar_f     | VARcharF     | YES      |
|  varchar_f     | varchar_x    | NO       |
|  varchar_f     | varcharX     | NO       |
```java
@Table(value = "dummy", autoColumnDetection = true)
public class Dummy implements Serializable {

  private Long id;

  private Integer intF;

  private Double decimalF;

  private Date datetimeF;

  private String varcharF;

  // getter setter omitted...
```

## Other Functionalities

### Java file generation
Manually compose Java Bean files from database table definition can be both tedious and error prone, datazilla provides a small tool to take over this job.

Place a file named "Table2JavaMeta.json" (see below) inside your project's root directory, execute the main method inside `Table2Java.java`, and the Java files will be generated at this location: \[your project root/\<srcRoot\>/\<domainPackage\>\]. Based on the example json below, a file named "Dummy.java" can be located at "your project root/src/test/java/com/github/haflife3/dataobject/Dummy.java" after a successful generation.

**`TIP:`** An IntelliJ IDEA plugin is also available: [table2java](https://plugins.jetbrains.com/plugin/14850-table2java/)

`Table2JavaMeta.json`
```json
{
  "driver":"com.mysql.jdbc.Driver",
  "dbUrl":"jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8",
  "dbUser":"root",
  "dbPass":"",
  "srcRoot":"/src/test/java",
  "domainPackage":"com.github.haflife3.dataobject",
  "autoColumnDetection": true,
  "extraTypeMap":{

  },
  "table2ClassMap":{
    "dummy":"Dummy"
  }
}
```

### transaction support
By wrapping your dataSource in a `TransactionAwareDataSourceProxy`, datazilla can fully support Spring's Transaction Management.

Please refer to [this doc](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/data-access.html#transaction) for more information.
```java
QueryEntry queryEntry = new QueryEntry(new TransactionAwareDataSourceProxy(dataSource));
```