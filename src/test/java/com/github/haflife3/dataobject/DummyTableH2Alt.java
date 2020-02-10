package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**  */
@Table(value = "DUMMY_TABLE", autoColumnDetection = true)
@Data
public class DummyTableH2Alt extends DummyTable{

  /**  */
  @PodamExclude
  //@TblField("ID")
  private Long id;

  /**  */
  @PodamIntValue(maxValue = 10)
  //@TblField("INT_F")
  private Integer intF;

  /**  */
  @PodamIntValue(maxValue = 50)
  //@TblField("INTEGER_F")
  private Integer integerF;

  /**  */
  //@TblField("BOOLEAN_F")
  private Boolean booleanF;

  /**  */
  //@TblField("BIT_F")
  private Boolean bitF;

  /**  */
  @PodamIntValue(maxValue = 5)
  //@TblField("TINYINT_F")
  private Byte tinyintF;

  /**  */
  @PodamIntValue(maxValue = 10)
  //@TblField("SMALLINT_F")
  private Short smallintF;

  /**  */
  @PodamIntValue(minValue = 2000,maxValue = 2090)
  //@TblField("YEAR_F")
  private Short yearF;

  /**  */
  //@TblField("DECIMAL_F")
  private BigDecimal decimalF;

  /**  */
  @PodamDoubleValue(maxValue = 999.0)
  //@TblField("DOUBLE_F")
  private Double doubleF;

  /**  */
  //@TblField("TIME_F")
  private Date timeF;

  /**  */
  //@TblField("DATE_F")
  private Date dateF;

  /**  */
  //@TblField("TIMESTAMP_F")
  private Date timestampF;

  /**  */
  //@TblField("DATETIME_F")
  private Date datetimeF;

  /**  */
  @PodamStringValue(length = 5)
  //@TblField("VARCHAR_F")
  private String varcharF;

  /**  */
  @PodamCharValue(maxValue = 'Z')
  //@TblField("CHAR_F")
  private String charF;

  /**  */
  //@TblField("UUID_F")
  private UUID uuidF;

}