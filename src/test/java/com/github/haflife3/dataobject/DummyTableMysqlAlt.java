package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.io.Serializable;
import java.util.Date;

/**  */
@Table(value = "dummy_table",autoColumnDetection = true)
@Data
public class DummyTableMysqlAlt extends DummyTable implements Serializable {

  /**  */
  @PodamExclude
  //@Column("id")
  private Long id;

  /**  */
  @PodamIntValue(maxValue = 5)
  //@Column("tinyint_f")
  private Integer tinyintF;

  /**  */
  @PodamIntValue(maxValue = 10)
  //@Column("smallint_f")
  private Integer smallintF;

  /**  */
  @PodamIntValue(minValue = 2000,maxValue = 2090)
  //@Column("year_f")
  private Integer yearF;

  /**  */
  @PodamIntValue(minValue = 1,maxValue = 10000)
  //@Column("int_f")
  private Integer intF;

  /**  */
  //@Column("bit_f")
  private Boolean bitF;

  /**  */
  @PodamLongValue(maxValue = 999999)
  //@Column("bigint_f")
  private Long bigintF;

  /**  */
  @PodamDoubleValue(maxValue = 999.0)
  //@Column("float_f")
  private Double floatF;

  /**  */
  @PodamDoubleValue(maxValue = 9999.0)
  //@Column("double_f")
  private Double doubleF;

  /**  */
  @PodamDoubleValue(maxValue = 9999.0)
  //@Column("decimal_f")
  private Double decimalF;

  /**  */
  @PodamDoubleValue(maxValue = 9999.0)
  //@Column("numeric_f")
  private Double numericF;

  /**  */
  //@Column("dateTime_f")
  private Date datetimeF;

  /**  */
  //@Column("timestamp_f")
  private Date timestampF;

  /**  */
  //@Column("date_f")
  private Date dateF;

  /**  */
  //@Column("time_f")
  private Date timeF;

  /**  */
  @PodamCharValue(maxValue = 'Z')
  //@Column("char_f")
  private String charF;

  /**  */
  @PodamStringValue(length = 5)
  //@Column("varchar_f")
  private String varcharF;

  @PodamStringValue(length = 5)
  @Column("name_mismatch_f")
  private String mismatchedName;

  /**  */
  @PodamStringValue(length = 20)
  //@Column("text_f")
  private String textF;

  /**  */
  @PodamStringValue(length = 100)
  //@Column("longtext_f")
  private String longtextF;

}