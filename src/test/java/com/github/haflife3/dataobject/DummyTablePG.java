package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.TblField;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Table("dummy_table")
@Data
public class DummyTablePG extends DummyTable{

  @PodamExclude
  @TblField("id")
  private Long id;

  @PodamLongValue(maxValue = 999999)
  @TblField("bigint_f")
  private Long bigintF;

  @PodamIntValue(maxValue = 10)
  @TblField("smallint_f")
  private Integer smallintF;

  @PodamIntValue(maxValue = 100)
  @TblField("integer_f")
  private Integer integerF;

  @PodamDoubleValue(maxValue = 999.0)
  @TblField("decimal_f")
  private Double decimalF;

  @PodamDoubleValue(maxValue = 999.0)
  @TblField("numeric_f")
  private Double numericF;

  @PodamDoubleValue(maxValue = 999.0)
  @TblField("real_f")
  private Double realF;
//
  @PodamDoubleValue(maxValue = 999.0)
  @TblField("double_precision_f")
  private Double doublePrecisionF;

  @PodamDoubleValue(maxValue = 999.0)
  @TblField("float8_f")
  private Double float8F;

  @PodamStringValue(length = 10)
  @TblField("varchar_f")
  private String varcharF;

  @PodamStringValue(length = 5)
  @TblField("character_f")
  private String characterF;

  @PodamCharValue(maxValue = 'Z')
  @TblField("char_f")
  private String charF;

  @PodamStringValue(length = 20)
  @TblField("text_f")
  private String textF;

  @PodamExclude
  @TblField("timestamp_f")
  private Timestamp timestampF;

  @TblField("date_f")
  private Date dateF;

  @TblField("time_f")
  private Time timeF;

  @TblField("boolean_f")
  private Boolean booleanF;

}