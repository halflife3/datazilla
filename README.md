# datazilla
datazilla is a Java library for relational database access. 
It is based on and inspired by [Apache Commons DbUtils](https://commons.apache.org/proper/commons-dbutils/).

As yet another ORM framework, you can use it to interact with databases in a way you would with other Java APIs, and in most cases you don't need to worry about the SQL part.

It comes with a minimum configuration requirement, and a set of user-friendly out of box APIs to help you with simplifying your code regarding database operations.

Requirement: JDK1.8+ .


Specially optimized for MySQL, PostgreSql, H2, SQLite, HsqlDb.
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
    * [auto column detection](#auto-column-detection)
    * [batch insert](#batch-insert)
    * [persist](#persist)
    * [paging and offset](#paging-and-offset)
    * [sql identify](#sql-identify)
    * [column selection](#column-selection)
    * [custom column handler](#custom-column-handler)
    * [custom result set handler](#custom-result-set-handler)
  * [Other functionalities](#Other-functionalities)
    * [Java file generation](#Java-file-generation)
    * [transaction support](#transaction-support)

# Maven Setting
datazilla is available in maven central repo.
```xml
<dependency>
    <groupId>com.github.haflife3</groupId>
    <artifactId>datazilla</artifactId>
    <version>1.1.12</version>
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
```sql
CREATE TABLE IF NOT EXISTS `dummy`  ( 
   `id`            bigint(20) AUTO_INCREMENT NOT NULL,
   `int_f`         int(11) NULL,
   `decimal_f`     decimal(10,5) NULL,
   `dateTime_f`    datetime NULL,
   `varchar_f`     varchar(200) NULL,
    PRIMARY KEY(id)
)
```
```java
@Table("dummy")
public class Dummy {

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
To create a record and save it to database, simply instantiate the java object and fill it with data, then call `QueryEntry.insert()`.
```java
Dummy dummy = new Dummy();
dummy.setIntF(10);
dummy.setDecimalF(12.34);
dummy.setDatetimeF(new Date());
dummy.setVarcharF("some text");
queryEntry.insert(dummy);
```
Corresponding sql:
```sql
insert into dummy (int_f,decimal_f,dateTime_f,varchar_f) values(?,?,?,?) -- values[10,12.34,<new Date()>,"some text"]
```

### update
To update records by conditions (in this case, by one id), instantiate the java object and fill it with data you wish to modify, add conditions which will filter out the records in question, then call `QueryEntry.updateSelective()`. <span style="color:green">Do note this:</span> any null value fields will be ignored for record modification, if however, you wish to include those null value fields, you can use `QueryEntry.updateFull()` instead.

Tip: It's almost always a bad practice to have primitive typed fields be mapped to database columns, for those fields can never be null and may raise unexpected behaviours.
```java
Dummy dummy = new Dummy();
dummy.setVarcharF("other text");
queryEntry.updateSelective(dummy,new Cond("id",1L));
```
Corresponding sql:
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
dummies = queryEntry.findObjects(Dummy.class, new Cond.Builder().fieldName("id").compareOpr("is not null").build());
```
Corresponding sqls:
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
Corresponding sql:
```sql
delete from dummy where id = ? -- values[1]
```
Tip: Actually, there are more ways to implement those basic operations, such as you can specify the table name and a list of conditions to perform a deletion should you wish not to have a table related java object at all. Even further, you can directly execute sql queries and deal with result yourself. Those, of course, are not very ORM-ish. Feel free to explore `QueryEntry` and find more.

## Advanced Cases

### auto column detection

### batch insert

### persist

### paging and offset

### sql identify

### column selection

### custom column handler

### custom result set handler

## Other Functionalities

### Java file generation

### transaction support