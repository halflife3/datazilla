package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.TblField;
import lombok.Data;
import uk.co.jemos.podam.common.PodamDoubleValue;
import uk.co.jemos.podam.common.PodamExclude;
import uk.co.jemos.podam.common.PodamStringValue;

import java.io.Serializable;

/**  */
@Table("dummy_table")
@Data
public class DummyTableSqlite extends DummyTable implements Serializable {

  /**  */
  @PodamExclude
  @TblField("ID")
  private Long id;

  /**  */
  @PodamDoubleValue(maxValue = 999.0)
  @TblField("real_f")
  private Double realF;

  /**  */
  @PodamDoubleValue(maxValue = 999.0)
  @TblField("numeric_f")
  private Double numericF;

  /**  */
  @PodamStringValue(length = 5)
  @TblField("text_f")
  private String textF;

  @PodamStringValue(length = 5)
  @TblField("name_mismatch_f")
  private String mismatchedName;

}