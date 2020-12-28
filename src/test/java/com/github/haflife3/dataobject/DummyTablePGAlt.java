package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Table(value = "dummy_table",autoColumnDetection = true)
@Data
public class DummyTablePGAlt extends DummyTable implements Serializable {

  @PodamExclude
  //@Column("id")
  private Long id;

  @PodamLongValue(maxValue = 999999)
  //@Column("bigint_f")
  private Long bigintF;

  @PodamIntValue(maxValue = 10)
  //@Column("smallint_f")
  private Integer smallintF;

  @PodamIntValue(maxValue = 100)
  //@Column("integer_f")
  private Integer integerF;

  @PodamDoubleValue(maxValue = 999.0)
  //@Column("decimal_f")
  private Double decimalF;

  @PodamDoubleValue(maxValue = 999.0)
  //@Column("numeric_f")
  private Double numericF;

  @PodamDoubleValue(maxValue = 999.0)
  //@Column("real_f")
  private Double realF;
//
  @PodamDoubleValue(maxValue = 999.0)
  //@Column("double_precision_f")
  private Double doublePrecisionF;

  @PodamDoubleValue(maxValue = 999.0)
  //@Column("float8_f")
  private Double float8F;

  @PodamStringValue(length = 10)
  //@Column("varchar_f")
  private String varcharF;

  @PodamStringValue(length = 5)
  @Column("name_mismatch_f")
  private String mismatchedName;

  @PodamStringValue(length = 5)
  //@Column("character_f")
  private String characterF;

  @PodamCharValue(maxValue = 'Z')
  //@Column("char_f")
  private String charF;

  @PodamStringValue(length = 20)
  //@Column("text_f")
  private String textF;

  @PodamExclude
  //@Column("timestamp_f")
  private Timestamp timestampF;

  //@Column("date_f")
  private Date dateF;

  //@Column("time_f")
  private Time timeF;

  //@Column("boolean_f")
  private Boolean booleanF;

}