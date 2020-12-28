package com.github.haflife3.dataobject;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.Column;
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
  @Column("ID")
  private Long id;

  /**  */
  @PodamDoubleValue(maxValue = 999.0)
  @Column("real_f")
  private Double realF;

  /**  */
  @PodamDoubleValue(maxValue = 999.0)
  @Column("numeric_f")
  private Double numericF;

  /**  */
  @PodamStringValue(length = 5)
  @Column("text_f")
  private String textF;

  @PodamStringValue(length = 5)
  @Column("name_mismatch_f")
  private String mismatchedName;

}