package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.TblField;
import lombok.Data;
import uk.co.jemos.podam.common.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**  */
@Table("dummy_table")
@Data
public class DummyTableSqlite extends DummyTable{

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

}