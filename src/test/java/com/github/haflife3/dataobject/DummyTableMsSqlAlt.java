package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.io.Serializable;
import java.util.Date;

@Table(value = "dummy_table", autoColumnDetection = true)
@Data
public class DummyTableMsSqlAlt extends DummyTable implements Serializable {

  @PodamExclude
  private Long id;

  @PodamBooleanValue
  private Boolean bitF;

  @PodamCharValue(maxValue = 'Z')
  private String charF;

  private Date dateF;

  private Date datetimeF;

  @PodamDoubleValue(maxValue = 9999.0)
  private Double decimalF;

  @PodamDoubleValue(maxValue = 99.0)
  private Double floatF;

  @PodamIntValue(minValue = 1,maxValue = 10000)
  private Integer intF;

  @PodamStringValue(length = 5)
  private String ncharF;

  @PodamStringValue(length = 10)
  private String ntextF;

  @PodamDoubleValue(maxValue = 9999.0)
  private Double numericF;

  @PodamStringValue(length = 10)
  private String nvarcharF;

  @PodamDoubleValue(maxValue = 9999.0)
  private Double realF;

  private Date smalldatetimeF;

  @PodamShortValue(minValue = 1,maxValue = 10)
  private Short smallintF;

  @PodamStringValue(length = 100)
  private String textF;

  @PodamShortValue(minValue = 1,maxValue = 10)
  private Short tinyintF;

  @PodamStringValue(length = 15)
  private String varcharF;

  @PodamStringValue(length = 5)
  @Column("name_mismatch_f")
  private String mismatchedName;

}