package com.github.haflife3.test.table2java

import com.github.haflife3.datazilla.dialect.DialectConst
import com.github.haflife3.datazilla.pojo.Table2JavaMeta
import com.github.haflife3.test.CommonInfo

class ValidationBank {

    static String mysqlJavaFileContent = """package com.github.haflife3.dataobject;

import java.io.Serializable;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import java.util.Date;

/** table comment */
@Table("test_gen_mysql")
public class TestGenMySQL implements Serializable {

  /** id comment */
  @Column("id")
  private Long id;

  /** tinyint comment */
  @Column("tinyint_f")
  private Integer tinyintF;

  @Column("smallint_f")
  private Integer smallintF;

  @Column("year_f")
  private Integer yearF;

  @Column("int_f")
  private Integer intF;

  @Column("bit_f")
  private Boolean bitF;

  @Column("bigint_f")
  private Long bigintF;

  @Column("float_f")
  private Double floatF;

  @Column("double_f")
  private Double doubleF;

  @Column("decimal_f")
  private Double decimalF;

  @Column("numeric_f")
  private Double numericF;

  @Column("dateTime_f")
  private Date datetimeF;

  @Column("timestamp_f")
  private Date timestampF;

  @Column("date_f")
  private Date dateF;

  @Column("time_f")
  private Date timeF;

  @Column("char_f")
  private String charF;

  @Column("varchar_f")
  private String varcharF;

  @Column("name_mismatch_f")
  private String nameMismatchF;

  @Column("text_f")
  private String textF;

  @Column("longtext_f")
  private String longtextF;

  public TestGenMySQL(){
  }

  private TestGenMySQL(Builder builder) {
    setId(builder.id);
    setTinyintF(builder.tinyintF);
    setSmallintF(builder.smallintF);
    setYearF(builder.yearF);
    setIntF(builder.intF);
    setBitF(builder.bitF);
    setBigintF(builder.bigintF);
    setFloatF(builder.floatF);
    setDoubleF(builder.doubleF);
    setDecimalF(builder.decimalF);
    setNumericF(builder.numericF);
    setDatetimeF(builder.datetimeF);
    setTimestampF(builder.timestampF);
    setDateF(builder.dateF);
    setTimeF(builder.timeF);
    setCharF(builder.charF);
    setVarcharF(builder.varcharF);
    setNameMismatchF(builder.nameMismatchF);
    setTextF(builder.textF);
    setLongtextF(builder.longtextF);
  }


  public Long getId() {
    return id;
  }

  public void setId( Long id ) {
    this.id = id;
  }

  public Integer getTinyintF() {
    return tinyintF;
  }

  public void setTinyintF( Integer tinyintF ) {
    this.tinyintF = tinyintF;
  }

  public Integer getSmallintF() {
    return smallintF;
  }

  public void setSmallintF( Integer smallintF ) {
    this.smallintF = smallintF;
  }

  public Integer getYearF() {
    return yearF;
  }

  public void setYearF( Integer yearF ) {
    this.yearF = yearF;
  }

  public Integer getIntF() {
    return intF;
  }

  public void setIntF( Integer intF ) {
    this.intF = intF;
  }

  public Boolean getBitF() {
    return bitF;
  }

  public void setBitF( Boolean bitF ) {
    this.bitF = bitF;
  }

  public Long getBigintF() {
    return bigintF;
  }

  public void setBigintF( Long bigintF ) {
    this.bigintF = bigintF;
  }

  public Double getFloatF() {
    return floatF;
  }

  public void setFloatF( Double floatF ) {
    this.floatF = floatF;
  }

  public Double getDoubleF() {
    return doubleF;
  }

  public void setDoubleF( Double doubleF ) {
    this.doubleF = doubleF;
  }

  public Double getDecimalF() {
    return decimalF;
  }

  public void setDecimalF( Double decimalF ) {
    this.decimalF = decimalF;
  }

  public Double getNumericF() {
    return numericF;
  }

  public void setNumericF( Double numericF ) {
    this.numericF = numericF;
  }

  public Date getDatetimeF() {
    return datetimeF;
  }

  public void setDatetimeF( Date datetimeF ) {
    this.datetimeF = datetimeF;
  }

  public Date getTimestampF() {
    return timestampF;
  }

  public void setTimestampF( Date timestampF ) {
    this.timestampF = timestampF;
  }

  public Date getDateF() {
    return dateF;
  }

  public void setDateF( Date dateF ) {
    this.dateF = dateF;
  }

  public Date getTimeF() {
    return timeF;
  }

  public void setTimeF( Date timeF ) {
    this.timeF = timeF;
  }

  public String getCharF() {
    return charF;
  }

  public void setCharF( String charF ) {
    this.charF = charF;
  }

  public String getVarcharF() {
    return varcharF;
  }

  public void setVarcharF( String varcharF ) {
    this.varcharF = varcharF;
  }

  public String getNameMismatchF() {
    return nameMismatchF;
  }

  public void setNameMismatchF( String nameMismatchF ) {
    this.nameMismatchF = nameMismatchF;
  }

  public String getTextF() {
    return textF;
  }

  public void setTextF( String textF ) {
    this.textF = textF;
  }

  public String getLongtextF() {
    return longtextF;
  }

  public void setLongtextF( String longtextF ) {
    this.longtextF = longtextF;
  }

  public static Builder builder(){
    return new Builder();
  }

  public static final class Builder {
    private Long id;
    private Integer tinyintF;
    private Integer smallintF;
    private Integer yearF;
    private Integer intF;
    private Boolean bitF;
    private Long bigintF;
    private Double floatF;
    private Double doubleF;
    private Double decimalF;
    private Double numericF;
    private Date datetimeF;
    private Date timestampF;
    private Date dateF;
    private Date timeF;
    private String charF;
    private String varcharF;
    private String nameMismatchF;
    private String textF;
    private String longtextF;

    public Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder tinyintF(Integer tinyintF) {
      this.tinyintF = tinyintF;
      return this;
    }

    public Builder smallintF(Integer smallintF) {
      this.smallintF = smallintF;
      return this;
    }

    public Builder yearF(Integer yearF) {
      this.yearF = yearF;
      return this;
    }

    public Builder intF(Integer intF) {
      this.intF = intF;
      return this;
    }

    public Builder bitF(Boolean bitF) {
      this.bitF = bitF;
      return this;
    }

    public Builder bigintF(Long bigintF) {
      this.bigintF = bigintF;
      return this;
    }

    public Builder floatF(Double floatF) {
      this.floatF = floatF;
      return this;
    }

    public Builder doubleF(Double doubleF) {
      this.doubleF = doubleF;
      return this;
    }

    public Builder decimalF(Double decimalF) {
      this.decimalF = decimalF;
      return this;
    }

    public Builder numericF(Double numericF) {
      this.numericF = numericF;
      return this;
    }

    public Builder datetimeF(Date datetimeF) {
      this.datetimeF = datetimeF;
      return this;
    }

    public Builder timestampF(Date timestampF) {
      this.timestampF = timestampF;
      return this;
    }

    public Builder dateF(Date dateF) {
      this.dateF = dateF;
      return this;
    }

    public Builder timeF(Date timeF) {
      this.timeF = timeF;
      return this;
    }

    public Builder charF(String charF) {
      this.charF = charF;
      return this;
    }

    public Builder varcharF(String varcharF) {
      this.varcharF = varcharF;
      return this;
    }

    public Builder nameMismatchF(String nameMismatchF) {
      this.nameMismatchF = nameMismatchF;
      return this;
    }

    public Builder textF(String textF) {
      this.textF = textF;
      return this;
    }

    public Builder longtextF(String longtextF) {
      this.longtextF = longtextF;
      return this;
    }

    public TestGenMySQL build() {
      return new TestGenMySQL(this);
    }
  }
}"""

    static String pgJavaFileContent = """package com.github.haflife3.dataobject;

import java.io.Serializable;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;

/** table comment */
@Table("test_gen_postgresql")
public class TestGenPostgreSql implements Serializable {

  /** id comment */
  @Column("id")
  private Long id;

  /** bigint_f comment */
  @Column("bigint_f")
  private Long bigintF;

  @Column("smallint_f")
  private Integer smallintF;

  @Column("integer_f")
  private Integer integerF;

  @Column("decimal_f")
  private Double decimalF;

  @Column("numeric_f")
  private Double numericF;

  @Column("real_f")
  private Double realF;

  @Column("double_precision_f")
  private Double doublePrecisionF;

  @Column("float8_f")
  private Double float8F;

  @Column("varchar_f")
  private String varcharF;

  @Column("name_mismatch_f")
  private String nameMismatchF;

  @Column("character_f")
  private String characterF;

  @Column("char_f")
  private String charF;

  @Column("text_f")
  private String textF;

  @Column("timestamp_f")
  private Timestamp timestampF;

  @Column("date_f")
  private Date dateF;

  @Column("time_f")
  private Time timeF;

  @Column("boolean_f")
  private Boolean booleanF;

  public TestGenPostgreSql(){
  }

  private TestGenPostgreSql(Builder builder) {
    setId(builder.id);
    setBigintF(builder.bigintF);
    setSmallintF(builder.smallintF);
    setIntegerF(builder.integerF);
    setDecimalF(builder.decimalF);
    setNumericF(builder.numericF);
    setRealF(builder.realF);
    setDoublePrecisionF(builder.doublePrecisionF);
    setFloat8F(builder.float8F);
    setVarcharF(builder.varcharF);
    setNameMismatchF(builder.nameMismatchF);
    setCharacterF(builder.characterF);
    setCharF(builder.charF);
    setTextF(builder.textF);
    setTimestampF(builder.timestampF);
    setDateF(builder.dateF);
    setTimeF(builder.timeF);
    setBooleanF(builder.booleanF);
  }


  public Long getId() {
    return id;
  }

  public void setId( Long id ) {
    this.id = id;
  }

  public Long getBigintF() {
    return bigintF;
  }

  public void setBigintF( Long bigintF ) {
    this.bigintF = bigintF;
  }

  public Integer getSmallintF() {
    return smallintF;
  }

  public void setSmallintF( Integer smallintF ) {
    this.smallintF = smallintF;
  }

  public Integer getIntegerF() {
    return integerF;
  }

  public void setIntegerF( Integer integerF ) {
    this.integerF = integerF;
  }

  public Double getDecimalF() {
    return decimalF;
  }

  public void setDecimalF( Double decimalF ) {
    this.decimalF = decimalF;
  }

  public Double getNumericF() {
    return numericF;
  }

  public void setNumericF( Double numericF ) {
    this.numericF = numericF;
  }

  public Double getRealF() {
    return realF;
  }

  public void setRealF( Double realF ) {
    this.realF = realF;
  }

  public Double getDoublePrecisionF() {
    return doublePrecisionF;
  }

  public void setDoublePrecisionF( Double doublePrecisionF ) {
    this.doublePrecisionF = doublePrecisionF;
  }

  public Double getFloat8F() {
    return float8F;
  }

  public void setFloat8F( Double float8F ) {
    this.float8F = float8F;
  }

  public String getVarcharF() {
    return varcharF;
  }

  public void setVarcharF( String varcharF ) {
    this.varcharF = varcharF;
  }

  public String getNameMismatchF() {
    return nameMismatchF;
  }

  public void setNameMismatchF( String nameMismatchF ) {
    this.nameMismatchF = nameMismatchF;
  }

  public String getCharacterF() {
    return characterF;
  }

  public void setCharacterF( String characterF ) {
    this.characterF = characterF;
  }

  public String getCharF() {
    return charF;
  }

  public void setCharF( String charF ) {
    this.charF = charF;
  }

  public String getTextF() {
    return textF;
  }

  public void setTextF( String textF ) {
    this.textF = textF;
  }

  public Timestamp getTimestampF() {
    return timestampF;
  }

  public void setTimestampF( Timestamp timestampF ) {
    this.timestampF = timestampF;
  }

  public Date getDateF() {
    return dateF;
  }

  public void setDateF( Date dateF ) {
    this.dateF = dateF;
  }

  public Time getTimeF() {
    return timeF;
  }

  public void setTimeF( Time timeF ) {
    this.timeF = timeF;
  }

  public Boolean getBooleanF() {
    return booleanF;
  }

  public void setBooleanF( Boolean booleanF ) {
    this.booleanF = booleanF;
  }

  public static Builder builder(){
    return new Builder();
  }

  public static final class Builder {
    private Long id;
    private Long bigintF;
    private Integer smallintF;
    private Integer integerF;
    private Double decimalF;
    private Double numericF;
    private Double realF;
    private Double doublePrecisionF;
    private Double float8F;
    private String varcharF;
    private String nameMismatchF;
    private String characterF;
    private String charF;
    private String textF;
    private Timestamp timestampF;
    private Date dateF;
    private Time timeF;
    private Boolean booleanF;

    public Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder bigintF(Long bigintF) {
      this.bigintF = bigintF;
      return this;
    }

    public Builder smallintF(Integer smallintF) {
      this.smallintF = smallintF;
      return this;
    }

    public Builder integerF(Integer integerF) {
      this.integerF = integerF;
      return this;
    }

    public Builder decimalF(Double decimalF) {
      this.decimalF = decimalF;
      return this;
    }

    public Builder numericF(Double numericF) {
      this.numericF = numericF;
      return this;
    }

    public Builder realF(Double realF) {
      this.realF = realF;
      return this;
    }

    public Builder doublePrecisionF(Double doublePrecisionF) {
      this.doublePrecisionF = doublePrecisionF;
      return this;
    }

    public Builder float8F(Double float8F) {
      this.float8F = float8F;
      return this;
    }

    public Builder varcharF(String varcharF) {
      this.varcharF = varcharF;
      return this;
    }

    public Builder nameMismatchF(String nameMismatchF) {
      this.nameMismatchF = nameMismatchF;
      return this;
    }

    public Builder characterF(String characterF) {
      this.characterF = characterF;
      return this;
    }

    public Builder charF(String charF) {
      this.charF = charF;
      return this;
    }

    public Builder textF(String textF) {
      this.textF = textF;
      return this;
    }

    public Builder timestampF(Timestamp timestampF) {
      this.timestampF = timestampF;
      return this;
    }

    public Builder dateF(Date dateF) {
      this.dateF = dateF;
      return this;
    }

    public Builder timeF(Time timeF) {
      this.timeF = timeF;
      return this;
    }

    public Builder booleanF(Boolean booleanF) {
      this.booleanF = booleanF;
      return this;
    }

    public TestGenPostgreSql build() {
      return new TestGenPostgreSql(this);
    }
  }
}"""

    static String mssqlJavaFileContent = """package com.github.haflife3.dataobject;

import java.io.Serializable;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import java.util.Date;

@Table("test_gen_mssql")
public class TestGenMsSql implements Serializable {

  @Column("id")
  private Long id;

  @Column("bit_f")
  private Boolean bitF;

  @Column("char_f")
  private String charF;

  @Column("date_f")
  private Date dateF;

  @Column("datetime_f")
  private Date datetimeF;

  @Column("decimal_f")
  private Double decimalF;

  @Column("float_f")
  private Double floatF;

  @Column("int_f")
  private Integer intF;

  @Column("nchar_f")
  private String ncharF;

  @Column("ntext_f")
  private String ntextF;

  @Column("numeric_f")
  private Double numericF;

  @Column("nvarchar_f")
  private String nvarcharF;

  @Column("real_f")
  private Double realF;

  @Column("smalldatetime_f")
  private Date smalldatetimeF;

  @Column("smallint_f")
  private Short smallintF;

  @Column("text_f")
  private String textF;

  @Column("tinyint_f")
  private Short tinyintF;

  @Column("varchar_f")
  private String varcharF;

  @Column("name_mismatch_f")
  private String nameMismatchF;

  public TestGenMsSql(){
  }

  private TestGenMsSql(Builder builder) {
    setId(builder.id);
    setBitF(builder.bitF);
    setCharF(builder.charF);
    setDateF(builder.dateF);
    setDatetimeF(builder.datetimeF);
    setDecimalF(builder.decimalF);
    setFloatF(builder.floatF);
    setIntF(builder.intF);
    setNcharF(builder.ncharF);
    setNtextF(builder.ntextF);
    setNumericF(builder.numericF);
    setNvarcharF(builder.nvarcharF);
    setRealF(builder.realF);
    setSmalldatetimeF(builder.smalldatetimeF);
    setSmallintF(builder.smallintF);
    setTextF(builder.textF);
    setTinyintF(builder.tinyintF);
    setVarcharF(builder.varcharF);
    setNameMismatchF(builder.nameMismatchF);
  }


  public Long getId() {
    return id;
  }

  public void setId( Long id ) {
    this.id = id;
  }

  public Boolean getBitF() {
    return bitF;
  }

  public void setBitF( Boolean bitF ) {
    this.bitF = bitF;
  }

  public String getCharF() {
    return charF;
  }

  public void setCharF( String charF ) {
    this.charF = charF;
  }

  public Date getDateF() {
    return dateF;
  }

  public void setDateF( Date dateF ) {
    this.dateF = dateF;
  }

  public Date getDatetimeF() {
    return datetimeF;
  }

  public void setDatetimeF( Date datetimeF ) {
    this.datetimeF = datetimeF;
  }

  public Double getDecimalF() {
    return decimalF;
  }

  public void setDecimalF( Double decimalF ) {
    this.decimalF = decimalF;
  }

  public Double getFloatF() {
    return floatF;
  }

  public void setFloatF( Double floatF ) {
    this.floatF = floatF;
  }

  public Integer getIntF() {
    return intF;
  }

  public void setIntF( Integer intF ) {
    this.intF = intF;
  }

  public String getNcharF() {
    return ncharF;
  }

  public void setNcharF( String ncharF ) {
    this.ncharF = ncharF;
  }

  public String getNtextF() {
    return ntextF;
  }

  public void setNtextF( String ntextF ) {
    this.ntextF = ntextF;
  }

  public Double getNumericF() {
    return numericF;
  }

  public void setNumericF( Double numericF ) {
    this.numericF = numericF;
  }

  public String getNvarcharF() {
    return nvarcharF;
  }

  public void setNvarcharF( String nvarcharF ) {
    this.nvarcharF = nvarcharF;
  }

  public Double getRealF() {
    return realF;
  }

  public void setRealF( Double realF ) {
    this.realF = realF;
  }

  public Date getSmalldatetimeF() {
    return smalldatetimeF;
  }

  public void setSmalldatetimeF( Date smalldatetimeF ) {
    this.smalldatetimeF = smalldatetimeF;
  }

  public Short getSmallintF() {
    return smallintF;
  }

  public void setSmallintF( Short smallintF ) {
    this.smallintF = smallintF;
  }

  public String getTextF() {
    return textF;
  }

  public void setTextF( String textF ) {
    this.textF = textF;
  }

  public Short getTinyintF() {
    return tinyintF;
  }

  public void setTinyintF( Short tinyintF ) {
    this.tinyintF = tinyintF;
  }

  public String getVarcharF() {
    return varcharF;
  }

  public void setVarcharF( String varcharF ) {
    this.varcharF = varcharF;
  }

  public String getNameMismatchF() {
    return nameMismatchF;
  }

  public void setNameMismatchF( String nameMismatchF ) {
    this.nameMismatchF = nameMismatchF;
  }

  public static Builder builder(){
    return new Builder();
  }

  public static final class Builder {
    private Long id;
    private Boolean bitF;
    private String charF;
    private Date dateF;
    private Date datetimeF;
    private Double decimalF;
    private Double floatF;
    private Integer intF;
    private String ncharF;
    private String ntextF;
    private Double numericF;
    private String nvarcharF;
    private Double realF;
    private Date smalldatetimeF;
    private Short smallintF;
    private String textF;
    private Short tinyintF;
    private String varcharF;
    private String nameMismatchF;

    public Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder bitF(Boolean bitF) {
      this.bitF = bitF;
      return this;
    }

    public Builder charF(String charF) {
      this.charF = charF;
      return this;
    }

    public Builder dateF(Date dateF) {
      this.dateF = dateF;
      return this;
    }

    public Builder datetimeF(Date datetimeF) {
      this.datetimeF = datetimeF;
      return this;
    }

    public Builder decimalF(Double decimalF) {
      this.decimalF = decimalF;
      return this;
    }

    public Builder floatF(Double floatF) {
      this.floatF = floatF;
      return this;
    }

    public Builder intF(Integer intF) {
      this.intF = intF;
      return this;
    }

    public Builder ncharF(String ncharF) {
      this.ncharF = ncharF;
      return this;
    }

    public Builder ntextF(String ntextF) {
      this.ntextF = ntextF;
      return this;
    }

    public Builder numericF(Double numericF) {
      this.numericF = numericF;
      return this;
    }

    public Builder nvarcharF(String nvarcharF) {
      this.nvarcharF = nvarcharF;
      return this;
    }

    public Builder realF(Double realF) {
      this.realF = realF;
      return this;
    }

    public Builder smalldatetimeF(Date smalldatetimeF) {
      this.smalldatetimeF = smalldatetimeF;
      return this;
    }

    public Builder smallintF(Short smallintF) {
      this.smallintF = smallintF;
      return this;
    }

    public Builder textF(String textF) {
      this.textF = textF;
      return this;
    }

    public Builder tinyintF(Short tinyintF) {
      this.tinyintF = tinyintF;
      return this;
    }

    public Builder varcharF(String varcharF) {
      this.varcharF = varcharF;
      return this;
    }

    public Builder nameMismatchF(String nameMismatchF) {
      this.nameMismatchF = nameMismatchF;
      return this;
    }

    public TestGenMsSql build() {
      return new TestGenMsSql(this);
    }
  }
}"""

    static String h2JavaFileContent = """package com.github.haflife3.dataobject;

import java.io.Serializable;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Table("test_gen_h2")
public class TestGenH2 implements Serializable {

  @Column("ID")
  private Long id;

  @Column("INT_F")
  private Integer intF;

  @Column("INTEGER_F")
  private Integer integerF;

  @Column("BOOLEAN_F")
  private Boolean booleanF;

  @Column("BIT_F")
  private Boolean bitF;

  @Column("TINYINT_F")
  private Byte tinyintF;

  @Column("SMALLINT_F")
  private Short smallintF;

  @Column("YEAR_F")
  private Short yearF;

  @Column("DECIMAL_F")
  private BigDecimal decimalF;

  @Column("DOUBLE_F")
  private Double doubleF;

  @Column("TIME_F")
  private Date timeF;

  @Column("DATE_F")
  private Date dateF;

  @Column("TIMESTAMP_F")
  private Date timestampF;

  @Column("DATETIME_F")
  private Date datetimeF;

  @Column("VARCHAR_F")
  private String varcharF;

  @Column("NAME_MISMATCH_F")
  private String nameMismatchF;

  @Column("CHAR_F")
  private String charF;

  @Column("UUID_F")
  private UUID uuidF;

  public TestGenH2(){
  }

  private TestGenH2(Builder builder) {
    setId(builder.id);
    setIntF(builder.intF);
    setIntegerF(builder.integerF);
    setBooleanF(builder.booleanF);
    setBitF(builder.bitF);
    setTinyintF(builder.tinyintF);
    setSmallintF(builder.smallintF);
    setYearF(builder.yearF);
    setDecimalF(builder.decimalF);
    setDoubleF(builder.doubleF);
    setTimeF(builder.timeF);
    setDateF(builder.dateF);
    setTimestampF(builder.timestampF);
    setDatetimeF(builder.datetimeF);
    setVarcharF(builder.varcharF);
    setNameMismatchF(builder.nameMismatchF);
    setCharF(builder.charF);
    setUuidF(builder.uuidF);
  }


  public Long getId() {
    return id;
  }

  public void setId( Long id ) {
    this.id = id;
  }

  public Integer getIntF() {
    return intF;
  }

  public void setIntF( Integer intF ) {
    this.intF = intF;
  }

  public Integer getIntegerF() {
    return integerF;
  }

  public void setIntegerF( Integer integerF ) {
    this.integerF = integerF;
  }

  public Boolean getBooleanF() {
    return booleanF;
  }

  public void setBooleanF( Boolean booleanF ) {
    this.booleanF = booleanF;
  }

  public Boolean getBitF() {
    return bitF;
  }

  public void setBitF( Boolean bitF ) {
    this.bitF = bitF;
  }

  public Byte getTinyintF() {
    return tinyintF;
  }

  public void setTinyintF( Byte tinyintF ) {
    this.tinyintF = tinyintF;
  }

  public Short getSmallintF() {
    return smallintF;
  }

  public void setSmallintF( Short smallintF ) {
    this.smallintF = smallintF;
  }

  public Short getYearF() {
    return yearF;
  }

  public void setYearF( Short yearF ) {
    this.yearF = yearF;
  }

  public BigDecimal getDecimalF() {
    return decimalF;
  }

  public void setDecimalF( BigDecimal decimalF ) {
    this.decimalF = decimalF;
  }

  public Double getDoubleF() {
    return doubleF;
  }

  public void setDoubleF( Double doubleF ) {
    this.doubleF = doubleF;
  }

  public Date getTimeF() {
    return timeF;
  }

  public void setTimeF( Date timeF ) {
    this.timeF = timeF;
  }

  public Date getDateF() {
    return dateF;
  }

  public void setDateF( Date dateF ) {
    this.dateF = dateF;
  }

  public Date getTimestampF() {
    return timestampF;
  }

  public void setTimestampF( Date timestampF ) {
    this.timestampF = timestampF;
  }

  public Date getDatetimeF() {
    return datetimeF;
  }

  public void setDatetimeF( Date datetimeF ) {
    this.datetimeF = datetimeF;
  }

  public String getVarcharF() {
    return varcharF;
  }

  public void setVarcharF( String varcharF ) {
    this.varcharF = varcharF;
  }

  public String getNameMismatchF() {
    return nameMismatchF;
  }

  public void setNameMismatchF( String nameMismatchF ) {
    this.nameMismatchF = nameMismatchF;
  }

  public String getCharF() {
    return charF;
  }

  public void setCharF( String charF ) {
    this.charF = charF;
  }

  public UUID getUuidF() {
    return uuidF;
  }

  public void setUuidF( UUID uuidF ) {
    this.uuidF = uuidF;
  }

  public static Builder builder(){
    return new Builder();
  }

  public static final class Builder {
    private Long id;
    private Integer intF;
    private Integer integerF;
    private Boolean booleanF;
    private Boolean bitF;
    private Byte tinyintF;
    private Short smallintF;
    private Short yearF;
    private BigDecimal decimalF;
    private Double doubleF;
    private Date timeF;
    private Date dateF;
    private Date timestampF;
    private Date datetimeF;
    private String varcharF;
    private String nameMismatchF;
    private String charF;
    private UUID uuidF;

    public Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder intF(Integer intF) {
      this.intF = intF;
      return this;
    }

    public Builder integerF(Integer integerF) {
      this.integerF = integerF;
      return this;
    }

    public Builder booleanF(Boolean booleanF) {
      this.booleanF = booleanF;
      return this;
    }

    public Builder bitF(Boolean bitF) {
      this.bitF = bitF;
      return this;
    }

    public Builder tinyintF(Byte tinyintF) {
      this.tinyintF = tinyintF;
      return this;
    }

    public Builder smallintF(Short smallintF) {
      this.smallintF = smallintF;
      return this;
    }

    public Builder yearF(Short yearF) {
      this.yearF = yearF;
      return this;
    }

    public Builder decimalF(BigDecimal decimalF) {
      this.decimalF = decimalF;
      return this;
    }

    public Builder doubleF(Double doubleF) {
      this.doubleF = doubleF;
      return this;
    }

    public Builder timeF(Date timeF) {
      this.timeF = timeF;
      return this;
    }

    public Builder dateF(Date dateF) {
      this.dateF = dateF;
      return this;
    }

    public Builder timestampF(Date timestampF) {
      this.timestampF = timestampF;
      return this;
    }

    public Builder datetimeF(Date datetimeF) {
      this.datetimeF = datetimeF;
      return this;
    }

    public Builder varcharF(String varcharF) {
      this.varcharF = varcharF;
      return this;
    }

    public Builder nameMismatchF(String nameMismatchF) {
      this.nameMismatchF = nameMismatchF;
      return this;
    }

    public Builder charF(String charF) {
      this.charF = charF;
      return this;
    }

    public Builder uuidF(UUID uuidF) {
      this.uuidF = uuidF;
      return this;
    }

    public TestGenH2 build() {
      return new TestGenH2(this);
    }
  }
}"""

    static String hsqlDbJavaFileContent = """package com.github.haflife3.dataobject;

import java.io.Serializable;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import java.util.Date;
import java.util.UUID;

@Table("test_gen_hsqldb")
public class TestGenHsqlDb implements Serializable {

  @Column("ID")
  private Integer id;

  @Column("TINYINT_F")
  private Integer tinyintF;

  @Column("SMALLINT_F")
  private Integer smallintF;

  @Column("INTEGER_F")
  private Integer integerF;

  @Column("BIGINT_F")
  private Long bigintF;

  @Column("REAL_F")
  private Double realF;

  @Column("FLOAT_F")
  private Double floatF;

  @Column("DOUBLE_F")
  private Double doubleF;

  @Column("DECIMAL_F")
  private Double decimalF;

  @Column("NUMERIC_F")
  private Double numericF;

  @Column("BOOLEAN_F")
  private Boolean booleanF;

  @Column("DATETIME_F")
  private Date datetimeF;

  @Column("TIMESTAMP_F")
  private Date timestampF;

  @Column("DATE_F")
  private Date dateF;

  @Column("TIME_F")
  private Date timeF;

  @Column("CHAR_F")
  private String charF;

  @Column("VARCHAR_F")
  private String varcharF;

  @Column("NAME_MISMATCH_F")
  private String nameMismatchF;

  @Column("LONGVARCHAR_F")
  private String longvarcharF;

  @Column("UUID_F")
  private UUID uuidF;

  public TestGenHsqlDb(){
  }

  private TestGenHsqlDb(Builder builder) {
    setId(builder.id);
    setTinyintF(builder.tinyintF);
    setSmallintF(builder.smallintF);
    setIntegerF(builder.integerF);
    setBigintF(builder.bigintF);
    setRealF(builder.realF);
    setFloatF(builder.floatF);
    setDoubleF(builder.doubleF);
    setDecimalF(builder.decimalF);
    setNumericF(builder.numericF);
    setBooleanF(builder.booleanF);
    setDatetimeF(builder.datetimeF);
    setTimestampF(builder.timestampF);
    setDateF(builder.dateF);
    setTimeF(builder.timeF);
    setCharF(builder.charF);
    setVarcharF(builder.varcharF);
    setNameMismatchF(builder.nameMismatchF);
    setLongvarcharF(builder.longvarcharF);
    setUuidF(builder.uuidF);
  }


  public Integer getId() {
    return id;
  }

  public void setId( Integer id ) {
    this.id = id;
  }

  public Integer getTinyintF() {
    return tinyintF;
  }

  public void setTinyintF( Integer tinyintF ) {
    this.tinyintF = tinyintF;
  }

  public Integer getSmallintF() {
    return smallintF;
  }

  public void setSmallintF( Integer smallintF ) {
    this.smallintF = smallintF;
  }

  public Integer getIntegerF() {
    return integerF;
  }

  public void setIntegerF( Integer integerF ) {
    this.integerF = integerF;
  }

  public Long getBigintF() {
    return bigintF;
  }

  public void setBigintF( Long bigintF ) {
    this.bigintF = bigintF;
  }

  public Double getRealF() {
    return realF;
  }

  public void setRealF( Double realF ) {
    this.realF = realF;
  }

  public Double getFloatF() {
    return floatF;
  }

  public void setFloatF( Double floatF ) {
    this.floatF = floatF;
  }

  public Double getDoubleF() {
    return doubleF;
  }

  public void setDoubleF( Double doubleF ) {
    this.doubleF = doubleF;
  }

  public Double getDecimalF() {
    return decimalF;
  }

  public void setDecimalF( Double decimalF ) {
    this.decimalF = decimalF;
  }

  public Double getNumericF() {
    return numericF;
  }

  public void setNumericF( Double numericF ) {
    this.numericF = numericF;
  }

  public Boolean getBooleanF() {
    return booleanF;
  }

  public void setBooleanF( Boolean booleanF ) {
    this.booleanF = booleanF;
  }

  public Date getDatetimeF() {
    return datetimeF;
  }

  public void setDatetimeF( Date datetimeF ) {
    this.datetimeF = datetimeF;
  }

  public Date getTimestampF() {
    return timestampF;
  }

  public void setTimestampF( Date timestampF ) {
    this.timestampF = timestampF;
  }

  public Date getDateF() {
    return dateF;
  }

  public void setDateF( Date dateF ) {
    this.dateF = dateF;
  }

  public Date getTimeF() {
    return timeF;
  }

  public void setTimeF( Date timeF ) {
    this.timeF = timeF;
  }

  public String getCharF() {
    return charF;
  }

  public void setCharF( String charF ) {
    this.charF = charF;
  }

  public String getVarcharF() {
    return varcharF;
  }

  public void setVarcharF( String varcharF ) {
    this.varcharF = varcharF;
  }

  public String getNameMismatchF() {
    return nameMismatchF;
  }

  public void setNameMismatchF( String nameMismatchF ) {
    this.nameMismatchF = nameMismatchF;
  }

  public String getLongvarcharF() {
    return longvarcharF;
  }

  public void setLongvarcharF( String longvarcharF ) {
    this.longvarcharF = longvarcharF;
  }

  public UUID getUuidF() {
    return uuidF;
  }

  public void setUuidF( UUID uuidF ) {
    this.uuidF = uuidF;
  }

  public static Builder builder(){
    return new Builder();
  }

  public static final class Builder {
    private Integer id;
    private Integer tinyintF;
    private Integer smallintF;
    private Integer integerF;
    private Long bigintF;
    private Double realF;
    private Double floatF;
    private Double doubleF;
    private Double decimalF;
    private Double numericF;
    private Boolean booleanF;
    private Date datetimeF;
    private Date timestampF;
    private Date dateF;
    private Date timeF;
    private String charF;
    private String varcharF;
    private String nameMismatchF;
    private String longvarcharF;
    private UUID uuidF;

    public Builder() {}

    public Builder id(Integer id) {
      this.id = id;
      return this;
    }

    public Builder tinyintF(Integer tinyintF) {
      this.tinyintF = tinyintF;
      return this;
    }

    public Builder smallintF(Integer smallintF) {
      this.smallintF = smallintF;
      return this;
    }

    public Builder integerF(Integer integerF) {
      this.integerF = integerF;
      return this;
    }

    public Builder bigintF(Long bigintF) {
      this.bigintF = bigintF;
      return this;
    }

    public Builder realF(Double realF) {
      this.realF = realF;
      return this;
    }

    public Builder floatF(Double floatF) {
      this.floatF = floatF;
      return this;
    }

    public Builder doubleF(Double doubleF) {
      this.doubleF = doubleF;
      return this;
    }

    public Builder decimalF(Double decimalF) {
      this.decimalF = decimalF;
      return this;
    }

    public Builder numericF(Double numericF) {
      this.numericF = numericF;
      return this;
    }

    public Builder booleanF(Boolean booleanF) {
      this.booleanF = booleanF;
      return this;
    }

    public Builder datetimeF(Date datetimeF) {
      this.datetimeF = datetimeF;
      return this;
    }

    public Builder timestampF(Date timestampF) {
      this.timestampF = timestampF;
      return this;
    }

    public Builder dateF(Date dateF) {
      this.dateF = dateF;
      return this;
    }

    public Builder timeF(Date timeF) {
      this.timeF = timeF;
      return this;
    }

    public Builder charF(String charF) {
      this.charF = charF;
      return this;
    }

    public Builder varcharF(String varcharF) {
      this.varcharF = varcharF;
      return this;
    }

    public Builder nameMismatchF(String nameMismatchF) {
      this.nameMismatchF = nameMismatchF;
      return this;
    }

    public Builder longvarcharF(String longvarcharF) {
      this.longvarcharF = longvarcharF;
      return this;
    }

    public Builder uuidF(UUID uuidF) {
      this.uuidF = uuidF;
      return this;
    }

    public TestGenHsqlDb build() {
      return new TestGenHsqlDb(this);
    }
  }
}"""

    static String sqlliteJavaFileContent = """package com.github.haflife3.dataobject;

import java.io.Serializable;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;

@Table("test_gen_sqlite")
public class TestGenSQLite implements Serializable {

  @Column("id")
  private Long id;

  @Column("text_f")
  private String textF;

  @Column("name_mismatch_f")
  private String nameMismatchF;

  @Column("real_f")
  private Double realF;

  @Column("numeric_f")
  private Double numericF;

  public TestGenSQLite(){
  }

  private TestGenSQLite(Builder builder) {
    setId(builder.id);
    setTextF(builder.textF);
    setNameMismatchF(builder.nameMismatchF);
    setRealF(builder.realF);
    setNumericF(builder.numericF);
  }


  public Long getId() {
    return id;
  }

  public void setId( Long id ) {
    this.id = id;
  }

  public String getTextF() {
    return textF;
  }

  public void setTextF( String textF ) {
    this.textF = textF;
  }

  public String getNameMismatchF() {
    return nameMismatchF;
  }

  public void setNameMismatchF( String nameMismatchF ) {
    this.nameMismatchF = nameMismatchF;
  }

  public Double getRealF() {
    return realF;
  }

  public void setRealF( Double realF ) {
    this.realF = realF;
  }

  public Double getNumericF() {
    return numericF;
  }

  public void setNumericF( Double numericF ) {
    this.numericF = numericF;
  }

  public static Builder builder(){
    return new Builder();
  }

  public static final class Builder {
    private Long id;
    private String textF;
    private String nameMismatchF;
    private Double realF;
    private Double numericF;

    public Builder() {}

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder textF(String textF) {
      this.textF = textF;
      return this;
    }

    public Builder nameMismatchF(String nameMismatchF) {
      this.nameMismatchF = nameMismatchF;
      return this;
    }

    public Builder realF(Double realF) {
      this.realF = realF;
      return this;
    }

    public Builder numericF(Double numericF) {
      this.numericF = numericF;
      return this;
    }

    public TestGenSQLite build() {
      return new TestGenSQLite(this);
    }
  }
}"""

    static Map<String,String> javaFileContentMap = new HashMap<>()

    static {
        javaFileContentMap.put(DialectConst.MYSQL,mysqlJavaFileContent)
        javaFileContentMap.put(DialectConst.PG,pgJavaFileContent)
        javaFileContentMap.put(DialectConst.MSSQL,mssqlJavaFileContent)
        javaFileContentMap.put(DialectConst.H2,h2JavaFileContent)
        javaFileContentMap.put(DialectConst.HSQLDB,hsqlDbJavaFileContent)
        javaFileContentMap.put(DialectConst.SQLITE,sqlliteJavaFileContent)
    }

    static Table2JavaMeta getMeta(String dialect){
        String tableName = "test_gen_"+dialect.toLowerCase()
        String javaFileName = "TestGen"+dialect
        def connInfo = CommonInfo.connInfoMap.get(dialect)
        def driver = CommonInfo.driverClassNameMap.get(dialect)
        def createTableSql = CommonInfo.createTableMap.get(dialect).replace("TABLE_PLACEHOLDER",tableName)
        return new Table2JavaMeta(
            driver: driver,
            dbUrl: connInfo.url,
            dbUser: connInfo.username,
            dbPass: connInfo.password,
            srcRoot: "/src/test/java",
            domainPackage:"com.github.haflife3.dataobject",
            lombokMode:false,
            autoColumnDetection:false,
            tableCreateSqls: [createTableSql],
            table2ClassMap: [
                (tableName):javaFileName
            ]
        )
    }

}
