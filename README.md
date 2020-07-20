# datazilla
datazilla is a Java library for relational database access. 
It is based on and inspired by [Apache Commons DbUtils](https://commons.apache.org/proper/commons-dbutils/).


Requirement: JDK1.8+ .


Specially optimized for MySQL, PostgreSql, H2, SQLite, HsqlDb.
(paging, bulk insert, Table entity Java file generation)

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
  * [Advanced cases](#Advanced-cases)

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

The underlying DB type will be automatically determined.
```java
DataSource ds = ... // it can be any implementation of javax.sql.DataSource
QueryEntry queryEntry = new QueryEntry(ds);
```

## Simple Cases
First we create a table, and a corresponding Java bean file: [Java file generation](#generate-Java-file-from-table-definition)
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

  // getter setter ...
}
```

### insert
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
```java
Dummy dummy = new Dummy();
dummy.setId(1L);
queryEntry.delObjects(dummy);
```
Corresponding sql:
```sql
delete from dummy where id = ? -- values[1]
```

## Advanced cases

### batchInsert

### persist

### paging and offset

### sql identify

## Other functionalities

### generate Java file from table definition