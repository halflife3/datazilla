package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Primary;
import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.io.Serializable;
import java.util.Date;

@Table("dummy_table")
@Data
public class DummyTableMsSql extends DummyTable implements Serializable{

  @PodamExclude
  @Primary
  @Column("id")
  private Long id;

  @PodamBooleanValue
  @Column("bit_f")
  private Boolean bitF;

  @PodamCharValue(maxValue = 'Z')
  @Column("char_f")
  private String charF;

  @Column("date_f")
  private Date dateF;

  @Column("datetime_f")
  private Date datetimeF;

  @PodamDoubleValue(maxValue = 9999.0)
  @Column("decimal_f")
  private Double decimalF;

  @PodamDoubleValue(maxValue = 99.0)
  @Column("float_f")
  private Double floatF;

  @PodamIntValue(minValue = 1,maxValue = 10000)
  @Column("int_f")
  private Integer intF;

  @PodamStringValue(length = 5)
  @Column("nchar_f")
  private String ncharF;

  @PodamStringValue(length = 10)
  @Column("ntext_f")
  private String ntextF;

  @PodamDoubleValue(maxValue = 9999.0)
  @Column("numeric_f")
  private Double numericF;

  @PodamStringValue(length = 10)
  @Column("nvarchar_f")
  private String nvarcharF;

  @PodamDoubleValue(maxValue = 9999.0)
  @Column("real_f")
  private Double realF;

  @Column("smalldatetime_f")
  private Date smalldatetimeF;

  @PodamShortValue(minValue = 1,maxValue = 10)
  @Column("smallint_f")
  private Short smallintF;

  @PodamStringValue(length = 100)
  @Column("text_f")
  private String textF;

  @PodamShortValue(minValue = 1,maxValue = 10)
  @Column("tinyint_f")
  private Short tinyintF;

  @PodamStringValue(length = 15)
  @Column("varchar_f")
  private String varcharF;

  @PodamStringValue(length = 5)
  @Column("name_mismatch_f")
  private String mismatchedName;

}