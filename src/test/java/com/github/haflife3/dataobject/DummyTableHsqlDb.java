package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.TblField;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table("dummy_table")
@Data
public class DummyTableHsqlDb extends DummyTable implements Serializable {

  @PodamExclude
  @TblField("ID")
  private Integer id;

  @PodamIntValue(maxValue = 5)
  @TblField("TINYINT_F")
  private Integer tinyintF;

  @PodamIntValue(maxValue = 10)
  @TblField("SMALLINT_F")
  private Integer smallintF;

  @PodamIntValue(minValue = 1,maxValue = 10000)
  @TblField("INTEGER_F")
  private Integer integerF;

  @PodamLongValue(maxValue = 999999)
  @TblField("BIGINT_F")
  private Long bigintF;

  @PodamDoubleValue(maxValue = 9999.0)
  @TblField("REAL_F")
  private Double realF;

  @PodamDoubleValue(maxValue = 9999.0)
  @TblField("FLOAT_F")
  private Double floatF;

  @PodamDoubleValue(maxValue = 99999.0)
  @TblField("DOUBLE_F")
  private Double doubleF;

  @PodamDoubleValue(maxValue = 99999.0)
  @TblField("DECIMAL_F")
  private Double decimalF;

  @PodamDoubleValue(maxValue = 99999.0)
  @TblField("NUMERIC_F")
  private Double numericF;

  @PodamBooleanValue
  @TblField("BOOLEAN_F")
  private Boolean booleanF;

  @TblField("DATETIME_F")
  private Date datetimeF;

  @TblField("TIMESTAMP_F")
  private Date timestampF;

  @TblField("DATE_F")
  private Date dateF;

  @TblField("TIME_F")
  private Date timeF;

  @PodamCharValue(maxValue = 'Z')
  @TblField("CHAR_F")
  private String charF;

  @PodamStringValue(length = 5)
  @TblField("VARCHAR_F")
  private String varcharF;

  @PodamStringValue(length = 5)
  @TblField("name_mismatch_f")
  private String mismatchedName;

  @PodamStringValue(length = 100)
  @TblField("LONGVARCHAR_F")
  private String longvarcharF;

  @TblField("UUID_F")
  private UUID uuidF;

}