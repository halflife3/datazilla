package com.github.haflife3.dataobject

import com.github.haflife3.datazilla.annotation.Table
import com.github.haflife3.datazilla.annotation.TblField
import uk.co.jemos.podam.common.PodamDoubleValue
import uk.co.jemos.podam.common.PodamExclude
import uk.co.jemos.podam.common.PodamStringValue

@Table("dummy_table")
class DummyTableSqliteGv extends DummyTable{

    /**  */
    @PodamExclude
    @TblField("ID")
    private Long id

    /**  */
    @PodamDoubleValue(maxValue = 999.0D)
    @TblField("real_f")
    private Double realF

    /**  */
    @PodamDoubleValue(maxValue = 999.0D)
    @TblField("numeric_f")
    private Double numericF

    /**  */
    @PodamStringValue(length = 5)
    @TblField("text_f")
    private String textF

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Double getRealF() {
        return realF
    }

    void setRealF(Double realF) {
        this.realF = realF
    }

    Double getNumericF() {
        return numericF
    }

    void setNumericF(Double numericF) {
        this.numericF = numericF
    }

    String getTextF() {
        return textF
    }

    void setTextF(String textF) {
        this.textF = textF
    }
}
