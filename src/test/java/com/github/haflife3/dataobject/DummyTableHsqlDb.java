package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Table("dummy_table")
@Data
public class DummyTableHsqlDb extends DummyTable implements Serializable {

  @PodamExclude
  @Column("ID")
  private Integer id;

  @PodamIntValue(maxValue = 5)
  @Column("TINYINT_F")
  private Integer tinyintF;

  @PodamIntValue(maxValue = 10)
  @Column("SMALLINT_F")
  private Integer smallintF;

  @PodamIntValue(minValue = 1,maxValue = 10000)
  @Column("INTEGER_F")
  private Integer integerF;

  @PodamLongValue(maxValue = 999999)
  @Column("BIGINT_F")
  private Long bigintF;

  @PodamDoubleValue(maxValue = 9999.0)
  @Column("REAL_F")
  private Double realF;

  @PodamDoubleValue(maxValue = 9999.0)
  @Column("FLOAT_F")
  private Double floatF;

  @PodamDoubleValue(maxValue = 99999.0)
  @Column("DOUBLE_F")
  private Double doubleF;

  @PodamDoubleValue(maxValue = 99999.0)
  @Column("DECIMAL_F")
  private Double decimalF;

  @PodamDoubleValue(maxValue = 99999.0)
  @Column("NUMERIC_F")
  private Double numericF;

  @PodamBooleanValue
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

  @PodamCharValue(maxValue = 'Z')
  @Column("CHAR_F")
  private String charF;

  @PodamStringValue(length = 5)
  @Column("VARCHAR_F")
  private String varcharF;

  @PodamStringValue(length = 5)
  @Column("name_mismatch_f")
  private String mismatchedName;

  @PodamStringValue(length = 100)
  @Column("LONGVARCHAR_F")
  private String longvarcharF;

  @Column("UUID_F")
  private UUID uuidF;

}