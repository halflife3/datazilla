package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Primary;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**  */
@Table("DUMMY_TABLE")
@Data
public class DummyTableH2 extends DummyTable implements Serializable {

  /**  */
  @PodamExclude
  @Primary
  @Column("ID")
  private Long id;

  /**  */
  @PodamIntValue(maxValue = 10)
  @Column("INT_F")
  private Integer intF;

  /**  */
  @PodamIntValue(maxValue = 50)
  @Column("INTEGER_F")
  private Integer integerF;

  /**  */
  @Column("BOOLEAN_F")
  private Boolean booleanF;

  /**  */
  @Column("BIT_F")
  private Boolean bitF;

  /**  */
  @PodamIntValue(maxValue = 5)
  @Column("TINYINT_F")
  private Byte tinyintF;

  /**  */
  @PodamIntValue(maxValue = 10)
  @Column("SMALLINT_F")
  private Short smallintF;

  /**  */
  @PodamIntValue(minValue = 2000,maxValue = 2090)
  @Column("YEAR_F")
  private Short yearF;

  /**  */
  @Column("DECIMAL_F")
  private BigDecimal decimalF;

  /**  */
  @PodamDoubleValue(maxValue = 999.0)
  @Column("DOUBLE_F")
  private Double doubleF;

  /**  */
  @Column("TIME_F")
  private Date timeF;

  /**  */
  @Column("DATE_F")
  private Date dateF;

  /**  */
  @Column("TIMESTAMP_F")
  private Date timestampF;

  /**  */
  @Column("DATETIME_F")
  private Date datetimeF;

  /**  */
  @PodamStringValue(length = 5)
  @Column("VARCHAR_F")
  private String varcharF;

  @PodamStringValue(length = 5)
  @Column("name_mismatch_f")
  private String mismatchedName;

  /**  */
  @PodamCharValue(maxValue = 'Z')
  @Column("CHAR_F")
  private String charF;

  /**  */
  @Column("UUID_F")
  private UUID uuidF;

}