package com.github.haflife3.dataobject

import com.github.haflife3.datazilla.annotation.Table
import com.github.haflife3.datazilla.annotation.TblField
import uk.co.jemos.podam.common.PodamCharValue
import uk.co.jemos.podam.common.PodamDoubleValue
import uk.co.jemos.podam.common.PodamExclude
import uk.co.jemos.podam.common.PodamIntValue
import uk.co.jemos.podam.common.PodamLongValue
import uk.co.jemos.podam.common.PodamStringValue

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp

@Table("dummy_table")
class DummyTablePGGv extends DummyTable implements Serializable{

    @PodamExclude
    @TblField("id")
    private Long id

    @PodamLongValue(maxValue = 999999L)
    @TblField("bigint_f")
    private Long bigintF

    @PodamIntValue(maxValue = 10)
    @TblField("smallint_f")
    private Integer smallintF

    @PodamIntValue(maxValue = 100)
    @TblField("integer_f")
    private Integer integerF

    @PodamDoubleValue(maxValue = 999.0D)
    @TblField("decimal_f")
    private Double decimalF

    @PodamDoubleValue(maxValue = 999.0D)
    @TblField("numeric_f")
    private Double numericF

    @PodamDoubleValue(maxValue = 999.0D)
    @TblField("real_f")
    private Double realF
//
    @PodamDoubleValue(maxValue = 999.0D)
    @TblField("double_precision_f")
    private Double doublePrecisionF

    @PodamDoubleValue(maxValue = 999.0D)
    @TblField("float8_f")
    private Double float8F

    @PodamStringValue(length = 10)
    @TblField("varchar_f")
    private String varcharF

    @PodamStringValue(length = 5)
    @TblField("name_mismatch_f")
    private String mismatchedName

    @PodamStringValue(length = 5)
    @TblField("character_f")
    private String characterF

    @PodamCharValue
    @TblField("char_f")
    private String charF

    @PodamStringValue(length = 20)
    @TblField("text_f")
    private String textF

    @PodamExclude
    @TblField("timestamp_f")
    private Timestamp timestampF

    @TblField("date_f")
    private Date dateF

    @TblField("time_f")
    private Time timeF

    @TblField("boolean_f")
    private Boolean booleanF

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Long getBigintF() {
        return bigintF
    }

    void setBigintF(Long bigintF) {
        this.bigintF = bigintF
    }

    Integer getSmallintF() {
        return smallintF
    }

    void setSmallintF(Integer smallintF) {
        this.smallintF = smallintF
    }

    Integer getIntegerF() {
        return integerF
    }

    void setIntegerF(Integer integerF) {
        this.integerF = integerF
    }

    Double getDecimalF() {
        return decimalF
    }

    void setDecimalF(Double decimalF) {
        this.decimalF = decimalF
    }

    Double getNumericF() {
        return numericF
    }

    void setNumericF(Double numericF) {
        this.numericF = numericF
    }

    Double getRealF() {
        return realF
    }

    void setRealF(Double realF) {
        this.realF = realF
    }

    Double getDoublePrecisionF() {
        return doublePrecisionF
    }

    void setDoublePrecisionF(Double doublePrecisionF) {
        this.doublePrecisionF = doublePrecisionF
    }

    Double getFloat8F() {
        return float8F
    }

    void setFloat8F(Double float8F) {
        this.float8F = float8F
    }

    String getVarcharF() {
        return varcharF
    }

    void setVarcharF(String varcharF) {
        this.varcharF = varcharF
    }

    String getCharacterF() {
        return characterF
    }

    void setCharacterF(String characterF) {
        this.characterF = characterF
    }

    String getCharF() {
        return charF
    }

    void setCharF(String charF) {
        this.charF = charF
    }

    String getTextF() {
        return textF
    }

    void setTextF(String textF) {
        this.textF = textF
    }

    Timestamp getTimestampF() {
        return timestampF
    }

    void setTimestampF(Timestamp timestampF) {
        this.timestampF = timestampF
    }

    Date getDateF() {
        return dateF
    }

    void setDateF(Date dateF) {
        this.dateF = dateF
    }

    Time getTimeF() {
        return timeF
    }

    void setTimeF(Time timeF) {
        this.timeF = timeF
    }

    Boolean getBooleanF() {
        return booleanF
    }

    void setBooleanF(Boolean booleanF) {
        this.booleanF = booleanF
    }

    String getMismatchedName() {
        return mismatchedName
    }

    void setMismatchedName(String mismatchedName) {
        this.mismatchedName = mismatchedName
    }
}
