package com.github.haflife3.dataobject;

import java.io.Serializable;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.TblField;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.util.Date;

@Table("dummy_table")
@Data
public class DummyTableMsSql extends DummyTable implements Serializable{

  @PodamExclude
  @TblField("id")
  private Long id;

  @PodamBooleanValue
  @TblField("bit_f")
  private Boolean bitF;

  @PodamCharValue(maxValue = 'Z')
  @TblField("char_f")
  private String charF;

  @TblField("date_f")
  private Date dateF;

  @TblField("datetime_f")
  private Date datetimeF;

  @PodamDoubleValue(maxValue = 9999.0)
  @TblField("decimal_f")
  private Double decimalF;

  @PodamDoubleValue(maxValue = 99.0)
  @TblField("float_f")
  private Double floatF;

  @PodamIntValue(minValue = 1,maxValue = 10000)
  @TblField("int_f")
  private Integer intF;

  @PodamStringValue(length = 5)
  @TblField("nchar_f")
  private String ncharF;

  @PodamStringValue(length = 10)
  @TblField("ntext_f")
  private String ntextF;

  @PodamDoubleValue(maxValue = 9999.0)
  @TblField("numeric_f")
  private Double numericF;

  @PodamStringValue(length = 10)
  @TblField("nvarchar_f")
  private String nvarcharF;

  @PodamDoubleValue(maxValue = 9999.0)
  @TblField("real_f")
  private Double realF;

  @TblField("smalldatetime_f")
  private Date smalldatetimeF;

  @PodamShortValue(minValue = 1,maxValue = 10)
  @TblField("smallint_f")
  private Short smallintF;

  @PodamStringValue(length = 100)
  @TblField("text_f")
  private String textF;

  @PodamShortValue(minValue = 1,maxValue = 10)
  @TblField("tinyint_f")
  private Short tinyintF;

  @PodamStringValue(length = 15)
  @TblField("varchar_f")
  private String varcharF;

  @PodamStringValue(length = 5)
  @TblField("name_mismatch_f")
  private String mismatchedName;

}