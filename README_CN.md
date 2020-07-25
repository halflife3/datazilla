# datazilla
datazilla 是一个可以用来操作关系型数据库的Java工具库.
受 [Apache Commons DbUtils](https://commons.apache.org/proper/commons-dbutils/) 启发并基于此而实现.

作为一个ORM框架, 你可以用操作常规Java API的方式来用其操作数据库, 绝大多数的情况下可以不必关注SQL部分.

仅需极少量的配置, 该工具就能提供很多开箱即用并友好的API来协助你简化和数据库操作相关的代码.

环境要求: JDK1.8+ .

对以下数据库有特殊优化: MySQL, PostgreSql, H2, SQLite, HsqlDb.(分页, 批量插入, 根据表结构生成Java文件)

Table of Contents
=================

* [Maven 配置](#Maven-配置)
* [用法示例](#用法示例)
  * [初始化](#初始化)
  * [基本用法](#基本用法)
    * [插入](#插入)
    * [更新](#更新)
    * [查询](#查询)
    * [删除](#删除)
  * [进阶用法](#进阶用法)
    * [批量插入](#批量插入)
    * [持久化](#持久化)
    * [分页及偏移](#分页及偏移)
    * [sql标注](#sql标注)
    * [选择查找的表字段](#选择查找的表字段)
    * [自定义结果处理器](#自定义结果处理器)
    * [自动表字段匹配](#自动表字段匹配)
  * [其他功能](#其他功能)
    * [Java文件生成](#Java文件生成)
    * [事务支持](#事务支持)

# Maven 配置
可在Maven中央仓库找到datazilla.
```xml
<dependency>
    <groupId>com.github.haflife3</groupId>
    <artifactId>datazilla</artifactId>
    <version>1.1.14</version>
</dependency>
```

# 用法示例
## 初始化
实例化QueryEntry时在构造方法里放入一个DataSource即可.

内部的数据库类型会被自动推断出来, 如果没找到符合的就会应用为默认值.
```java
DataSource ds = ... // it can be any implementation of javax.sql.DataSource
QueryEntry queryEntry = new QueryEntry(ds);
```

## 基本用法
首先我们创建一个表和对应的Java文件: [Java文件生成](#Java文件生成)

**`注意:`** 这里所有的SQL示例片段,包括DDL, DQL and DML, 都是以MySQL为准的, 在其他的数据库类型下会有些少量的语法变动, 但基本不会影响下面这些例子的有效性.
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
**`注意:`** 代码中`implements Serializable`这部分没特殊含义, 只是为了符合Java Bean的标准, 略去这部分不会影响任何功能. 
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

### 插入
要在数据库里插入一条数据, 仅需实例化一个Java对象并填充数据, 然后调用`QueryEntry.insert()`即可. 对象里所有null值都会被忽略, 数据库本身会对这些null值做默认值或留空处理. 这个例子中我们不设置字段id的值并交由数据库给其一个自增数字.
```java
Dummy dummy = new Dummy();
dummy.setIntF(10);
dummy.setDecimalF(12.34);
dummy.setDatetimeF(new Date());
dummy.setVarcharF("some text");
queryEntry.insert(dummy);
```
对应的SQL:
```sql
insert into dummy (int_f,decimal_f,dateTime_f,varchar_f) values(?,?,?,?) -- values[10,12.34,<new Date()>,"some text"]
```

### 更新
要根据条件更新数据库里的记录, 实例化一个Java对象并设置你需要修改的值, 然后添加过滤需要更新的记录的条件, 再调用`QueryEntry.updateSelective()`. 这里要注意所有的null值字段都会被忽略, 如果你确实需要更新这些null值字段, 考虑使用`QueryEntry.updateFull()`.

**`提示:`** 几乎所有场景下, 将原始类型的字段对应到表字段都是不好的做法, 因为这些字段没法为null, 由此可能产生意料之外的行为.
```java
Dummy dummy = new Dummy();
dummy.setVarcharF("other text");
queryEntry.updateSelective(dummy,new Cond("id",1L));
```
对应的SQL:
```sql
update dummy set varchar_f = ? where id = ? -- values["other text",1]
```

### 查询
有两组API可以用于数据查询: (searchObjects,searchObject) 和 (findObjects,findObject).

第一种方式更加面向对象, 第二种则更加灵活.

当使用 `QueryEntry.searchObjects()` 或 `QueryEntry.searchObject()`时, 传入的对象会被转换成一个条件列表 (null值字段会被忽略), 在内部仍旧会调用这个 `QueryEntry.findObjects()` 方法.
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
对应的SQLs:
```sql
select * from dummy where id = ? -- values[1]
select * from dummy where id = ? -- values[1]
select * from dummy where id > ? -- values[0]
select * from dummy where id = ? and int_f = ? -- values[1,10]
select * from dummy where id is not null
```

### 删除
和[查询](#查询)一样, 有两种方式来删除数据.

可以使用面向对象的方式, 也就是对象即条件的方式, 或者像代码中展示的那种更灵活的方式.
```java
Dummy dummy = new Dummy();
dummy.setId(1L);
queryEntry.delObjects(dummy);
//a more flexible way to delete
queryEntry.delObjects(Dummy.class, new Cond("id", 1));
```
对应的SQL:
```sql
delete from dummy where id = ? -- values[1]
```
**`提示:`** Actually, there are more ways to implement those basic operations, such as you can specify the table name and a list of conditions to perform a deletion should you wish not to have a table related Java class at all. Even further, you can directly execute sql queries and deal with result yourself. Those, of course, are not very ORM-ish. Feel free to explore `QueryEntry` and find more.

## 进阶用法
这部分中, 下面例子所用到的表和Java Bean仍旧沿用上面创建好的.

### 批量插入
To save multiple records to database at the same time, instantiate one or more Java objects and fill them with data, then call `QueryEntry.batchInsert()`. Null values are ignored as always. You can place records separately into the method call as `Varargs` or altogether as a list.

**`提示:`** The default bulk size for batch insert is 100, you can specify the size by place an integer number as the first method parameter (e.g: `QueryEntry.batchInsert(200, Arrays.asList(dummy1,dummy2,dummy3 ... ))`).

**`提示:`** It is recommended to use this inside a transaction scope. [transaction support](#transaction-support)

**`注意:`** The single `batchInsert` operation below is separated into two insert queries. datazilla will automatically group together those records which can be operated in a single query. The reason for this seemingly odd behaviour is that we must let database decide how to deal with the null values, `dummy3` here doesn't have `varcharF` set, if there is only one insert query, it would look like this: `insert into dummy (int_f,varchar_f) values (?,?) , ( ?,?), ( ?,?) -- values: [10, "some text", 20, "other text", 30, null]`. Note the last value is null, if `varchar_f` has a default value, the default value can never be applied. 
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
对应的SQL:
```sql
insert into dummy (int_f,varchar_f) values (?,?) , ( ?,?) -- values: [10, "some text", 20, "other text"]
insert into dummy (int_f) values (?)  -- values: [30]
```

### persist
To persist (same as upsert if you are more familiar with this term) a record into database, instantiate a Java object and fill it with data, specify the conditions to filter out the record to be updated if it ever exists, then call `QueryEntry.persist()`. datazilla will first try to perform an update operation by the conditions, if no database record is updated, the object will be inserted into database instead.

**`提示:`** It is recommended to use this inside a transaction scope. [transaction support](#transaction-support)
```java
Dummy dummy = new Dummy();
dummy.setIntF(40);
dummy.setVarcharF("some text");
queryEntry.persist(dummy,new Cond("id",10L));
```
对应的SQL:
```sql
update dummy set int_f=?,varchar_f=? where id = ?  -- values: [40, "some text", 10]
insert into dummy (int_f,varchar_f)  values( ?,?)   -- values: [40, "some text"]
```

### paging and offset
To query a subset of records filtered by conditions, and optionally get the total count, supply a paging(`ExtraParamInjector.paging()`) or an offset(`ExtraParamInjector.offset()`) instruction right before the query action(`QueryEntry.findObjects()` or `QueryEntry.searchObjects()`). The total count, if required, can be retrieved(`ExtraParamInjector.getTotalCount()`) right after the query action.

**`提示:`** `ExtraParamInjector.paging()` and `ExtraParamInjector.offset()` should be placed right before the related query action, and `ExtraParamInjector.getTotalCount()` right after it, or else they may be intercepted by other queries by accident and yield undesired result.

**`提示:`** It is recommended to use this inside a read-only transaction scope. [transaction support](#transaction-support)

**`注意:`**  The calculation formula of `pageNo, pageSize` to `offset, limit`: <offset = (pageNo - 1) * pageSize, limit = pageSize> .
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
对应的SQL:
```sql
-- paging by pageNo and pageSize
select * from dummy where id > ? order by id desc limit ?,?  -- values: [0, 5, 5]
select count(*) as count from dummy where id > ?  -- values: [0]

-- paging by offset and limit
select * from dummy where id > ? order by id desc limit ?,?   -- values: [0, 1, 2]
```

### sql identify
Due to datazilla's nature of constructing SQL at runtime, SQL query can be hard to trace back to, by adding a comment as its identity, tracing will no longer be as hard. 

**`提示:`** Place `ExtraParamInjector.sqlId()` right before your query action for the same reason as when you are using `ExtraParamInjector.paging()` and `ExtraParamInjector.offset()`.
```java
ExtraParamInjector.sqlId("find the dumbest dummy");
Dummy dummy = queryEntry.findObject(Dummy.class, new Cond("id", 1));
```
对应的SQL:
```sql
/* find the dumbest dummy */ select * from dummy where id = ?  -- values: [1]
```

### column selection
To specify which table columns to query instead of all, supply a list of column names in `ExtraParamInjector.selectColumns()` and place it right before the query action.

**`提示:`** Place `ExtraParamInjector.selectColumns()` right before your query action for the same reason as when you are using `ExtraParamInjector.paging()` and `ExtraParamInjector.offset()`.
```java
ExtraParamInjector.selectColumns("int_f","varchar_f");
Dummy dummy = queryEntry.findObject(Dummy.class, new Cond("id", 1));
```
对应的SQL:
```sql
select int_f, varchar_f from dummy where id = ?  -- values: [1]
```

### custom result set handler
To fully control the SQL constructing and result handling yourself, supply a predefined SQL, a `ResultSetHandler` implementation, and a list of values as positional parameters for the SQL(can be skipped if none is required), then call `QueryEntry.genericQry()`.

**`提示:`** See also `QueryEntry.genericUpdate` for custom update.
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
对应的SQL:
```sql
select * from dummy where id > ?  -- values: [0]
```

### auto column detection
If table column names and corresponding Java field names meet a [certain pattern](#match-pattern), they can be automatically paired by adding this configuration(`autoColumnDetection = true`) to `@Table` annotation, and no longer we have to add `@TblField` on each Java field.

**`提示:`** `@TblField` can still take precedence over `autoColumnDetection`.
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