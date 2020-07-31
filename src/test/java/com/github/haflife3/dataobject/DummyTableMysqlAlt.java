package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.TblField;
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
  //@TblField("id")
  private Long id;

  /**  */
  @PodamIntValue(maxValue = 5)
  //@TblField("tinyint_f")
  private Integer tinyintF;

  /**  */
  @PodamIntValue(maxValue = 10)
  //@TblField("smallint_f")
  private Integer smallintF;

  /**  */
  @PodamIntValue(minValue = 2000,maxValue = 2090)
  //@TblField("year_f")
  private Integer yearF;

  /**  */
  @PodamIntValue(minValue = 1,maxValue = 10000)
  //@TblField("int_f")
  private Integer intF;

  /**  */
  //@TblField("bit_f")
  private Boolean bitF;

  /**  */
  @PodamLongValue(maxValue = 999999)
  //@TblField("bigint_f")
  private Long bigintF;

  /**  */
  @PodamDoubleValue(maxValue = 999.0)
  //@TblField("float_f")
  private Double floatF;

  /**  */
  @PodamDoubleValue(maxValue = 9999.0)
  //@TblField("double_f")
  private Double doubleF;

  /**  */
  @PodamDoubleValue(maxValue = 9999.0)
  //@TblField("decimal_f")
  private Double decimalF;

  /**  */
  @PodamDoubleValue(maxValue = 9999.0)
  //@TblField("numeric_f")
  private Double numericF;

  /**  */
  //@TblField("dateTime_f")
  private Date datetimeF;

  /**  */
  //@TblField("timestamp_f")
  private Date timestampF;

  /**  */
  //@TblField("date_f")
  private Date dateF;

  /**  */
  //@TblField("time_f")
  private Date timeF;

  /**  */
  @PodamCharValue(maxValue = 'Z')
  //@TblField("char_f")
  private String charF;

  /**  */
  @PodamStringValue(length = 5)
  //@TblField("varchar_f")
  private String varcharF;

  @PodamStringValue(length = 5)
  @TblField("name_mismatch_f")
  private String mismatchedName;

  /**  */
  @PodamStringValue(length = 20)
  //@TblField("text_f")
  private String textF;

  /**  */
  @PodamStringValue(length = 100)
  //@TblField("longtext_f")
  private String longtextF;

}