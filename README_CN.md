# datazilla
datazilla 是一个可以用来操作关系型数据库的Java工具库.
受 [Apache Commons DbUtils](https://commons.apache.org/proper/commons-dbutils/) 启发并基于此而实现.

作为一个ORM框架, 你可以用操作常规Java API的方式来用其操作数据库, 绝大多数的情况下可以不必关注SQL部分.

仅需极少量的配置, 该工具就能提供很多开箱即用并友好的API来协助你简化和数据库操作相关的代码.

环境要求: JDK1.8+ .

对以下数据库有特殊优化: MySQL, PostgreSql, SQL Server(2012+), H2, SQLite, HsqlDb.(分页, 批量插入, 根据表结构生成Java文件)

目录
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
    <version>1.2.1</version>
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
**`提示:`** 还有很多其他方式来实现这些基本操作, 例如你不想要创建表对应的Java类的话, 可以直接指定表名和条件列表来操作删除. 更进一步, 你还可以直接执行SQL语句进行查询并自行处理返回的结果. 这类操作当然也不太符合ORM. 你可以在`QueryEntry`里找到更多的用法.

## 进阶用法
这部分中, 下面例子所用到的表和Java Bean仍旧沿用上面创建好的.

### 批量插入
要将一批数据同时插入数据库中, 实例化一个或多个Java对象并填充数据, 然后调用`QueryEntry.batchInsert()`. null值仍旧一样会被忽略. 你可以用可变参数列表的形式或者一整个list的形式来传参.

**`提示:`** 批量插入时默认批次数量是100, 你可以在方法的第一个参数指定其他的值 (例如: `QueryEntry.batchInsert(200, Arrays.asList(dummy1,dummy2,dummy3 ... ))`).

**`提示:`** 建议在事务作用域下使用. [事务支持](#事务支持)

**`注意:`** 下面代码示例的一个单次`batchInsert`调用会被分成两个SQL. datazilla会自动聚合可以在同一个语句中进行插入的数据. 这个看似奇怪的行为其实是因为针对null值的字段我们必须交给数据库自身去处理, 这里的`dummy3`里字段`varcharF`的值没设置, 如果只有一个SQL语句的话就会变成这种: ` insert into dummy (int_f,varchar_f) values (?,?) , ( ?,?), ( ?,?) -- values: [10, "some text", 20, "other text", 30, null]`. 注意最后一个值是null, 如果`varchar_f`有默认值的话那这个默认值就没法被应用了.
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

### 持久化
要将一个数据持久化(或者叫更新插入)到数据库中, 实例化一个Java对象并填充数据, 指定更新的条件, 然后调用`QueryEntry.persist()`. datazilla会首先尝试根据指定的条件更新, 如果没有数据最终被更新, 则执行插入操作.

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

### 分页及偏移
要从数据库获取一部分根据条件筛选的数据, 同时得到数据的总量(如果需要总量), 指定一个分页(`ExtraParamInjector.paging()`)或偏移(`ExtraParamInjector.paging()`)指令并放置在查询请求之前. 数据的总量(如果需要总量)可以在查询请求后用`ExtraParamInjector.getTotalCount()`获取

**`提示:`** `ExtraParamInjector.paging()` 和 `ExtraParamInjector.offset()` 最好放在紧挨着查询请求的前面, `ExtraParamInjector.getTotalCount()` 放在紧挨着查询请求的后面, 不然有可能会被其他查询请求拦截然后得到意料之外的结果.

**`提示:`** 建议在只读事务作用域下使用. [事务支持](#事务支持)

**`注意:`** `pageNo, pageSize` 到 `offset, limit`的算法公式: <offset = (pageNo - 1) * pageSize, limit = pageSize> .
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

### sql标注
由于datazilla本身会在运行时才组装SQL, 就不容易追查SQL语句的来源, 如果在语句前面加一个身份标签那就可以方便追查了. 

**`提示:`** 将`ExtraParamInjector.sqlId()`放在紧挨着查询操作的前面, 理由同`ExtraParamInjector.paging()` 和 `ExtraParamInjector.offset()`的使用方式.
```java
ExtraParamInjector.sqlId("find the dumbest dummy");
Dummy dummy = queryEntry.findObject(Dummy.class, new Cond("id", 1));
```
对应的SQL:
```sql
/* find the dumbest dummy */ select * from dummy where id = ?  -- values: [1]
```

### 选择查找的表字段
要指定查询某几个表字段, 在`ExtraParamInjector.selectColumns()`里指定字段列表并将其放在查询操作的前面.

**`提示:`** 将`ExtraParamInjector.selectColumns()`放在紧挨着查询操作的前面, 理由同`ExtraParamInjector.paging()` 和 `ExtraParamInjector.offset()`的使用方式.
```java
ExtraParamInjector.selectColumns("int_f","varchar_f");
Dummy dummy = queryEntry.findObject(Dummy.class, new Cond("id", 1));
```
对应的SQL:
```sql
select int_f, varchar_f from dummy where id = ?  -- values: [1]
```

### 自定义结果处理器
要完全自主控制SQL语句的组装以及结果的处理, 指定一个预先组装好的SQL和一个`ResultSetHandler`的实现以及对应SQL语句的位置参数列表(如果没有参数则可以忽略), 然后调用`QueryEntry.genericQry()`.

**`提示:`** 要使用自定义更新请参考`QueryEntry.genericUpdate`.
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

### 自动表字段匹配
如果表字段和Java的实例变量符合[特定的规则](#匹配规则),那就可以在`@Table`标签里设置`autoColumnDetection = true`来实现自动匹配, 这样我们就不必在每个Java字段前用`@TblField`标签了.

**`提示:`** `@TblField` 的优先级仍旧高于 `autoColumnDetection`.
#### 匹配规则
不区分大小写, 忽略下划线.

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

## 其他功能

### Java文件生成
根据表结构定义手工编写Java文件即麻烦又容易出错, datazilla提供了一个小工具来处理这部分工作.

将一个名为"Table2JavaMeta.json"的文件(参考下面)房子在工程主目录下面, 执行`Table2Java.java`里的main方法, Java文件就会在这个位置创建: \[your project root/\<srcRoot\>/\<domainPackage\>\]. 以下面的json为例, 生成成功后一个名为"Dummy.java"的文件会被放置在这里:"your project root/src/test/java/com/github/haflife3/dataobject/Dummy.java".

**`TIP:`** 可以配合IntelliJ IDEA的插件使用: [table2java](https://plugins.jetbrains.com/plugin/14850-table2java/)

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

### 事务支持
将数据源对象用`TransactionAwareDataSourceProxy`包装, datazilla 即可支持Spring的事务管理.

关于事务管理参见[这篇文档](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/data-access.html#transaction).
```java
QueryEntry queryEntry = new QueryEntry(new TransactionAwareDataSourceProxy(dataSource));
```
