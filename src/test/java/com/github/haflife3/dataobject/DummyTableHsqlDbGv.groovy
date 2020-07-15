package com.github.haflife3.dataobject

import com.github.haflife3.datazilla.annotation.Table
import com.github.haflife3.datazilla.annotation.TblField
import groovy.transform.PropertyOptions
import uk.co.jemos.podam.common.PodamBooleanValue
import uk.co.jemos.podam.common.PodamCharValue
import uk.co.jemos.podam.common.PodamDoubleValue
import uk.co.jemos.podam.common.PodamExclude
import uk.co.jemos.podam.common.PodamIntValue
import uk.co.jemos.podam.common.PodamLongValue
import uk.co.jemos.podam.common.PodamStringValue

@Table("dummy_table")
@PropertyOptions
class DummyTableHsqlDbGv extends DummyTable{

    @PodamExclude
    @TblField("ID")
    private Integer id

    @PodamIntValue(maxValue = 5)
    @TblField("TINYINT_F")
    private Integer tinyintF

    @PodamIntValue(maxValue = 10)
    @TblField("SMALLINT_F")
    private Integer smallintF

    @PodamIntValue(minValue = 1,maxValue = 10000)
    @TblField("INTEGER_F")
    private Integer integerF

    @PodamLongValue(maxValue = 999999L)
    @TblField("BIGINT_F")
    private Long bigintF

    @PodamDoubleValue(maxValue = 9999.0D)
    @TblField("REAL_F")
    private Double realF

    @PodamDoubleValue(maxValue = 9999.0D)
    @TblField("FLOAT_F")
    private Double floatF

    @PodamDoubleValue(maxValue = 99999.0D)
    @TblField("DOUBLE_F")
    private Double doubleF

    @PodamDoubleValue(maxValue = 99999.0D)
    @TblField("DECIMAL_F")
    private Double decimalF

    @PodamDoubleValue(maxValue = 99999.0D)
    @TblField("NUMERIC_F")
    private Double numericF

    @PodamBooleanValue
    @TblField("BOOLEAN_F")
    private Boolean booleanF

    @TblField("DATETIME_F")
    private Date datetimeF

    @TblField("TIMESTAMP_F")
    private Date timestampF

    @TblField("DATE_F")
    private Date dateF

    @TblField("TIME_F")
    private Date timeF

    @PodamCharValue
    @TblField("CHAR_F")
    private String charF

    @PodamStringValue(length = 5)
    @TblField("VARCHAR_F")
    private String varcharF

    @PodamStringValue(length = 5)
    @TblField("name_mismatch_f")
    private String mismatchedName

    @PodamStringValue(length = 100)
    @TblField("LONGVARCHAR_F")
    private String longvarcharF

    @TblField("UUID_F")
    private UUID uuidF

    Integer getId() {
        return id
    }

    void setId(Integer id) {
        this.id = id
    }

    Integer getTinyintF() {
        return tinyintF
    }

    void setTinyintF(Integer tinyintF) {
        this.tinyintF = tinyintF
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

    Long getBigintF() {
        return bigintF
    }

    void setBigintF(Long bigintF) {
        this.bigintF = bigintF
    }

    Double getRealF() {
        return realF
    }

    void setRealF(Double realF) {
        this.realF = realF
    }

    Double getFloatF() {
        return floatF
    }

    void setFloatF(Double floatF) {
        this.floatF = floatF
    }

    Double getDoubleF() {
        return doubleF
    }

    void setDoubleF(Double doubleF) {
        this.doubleF = doubleF
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

    Boolean getBooleanF() {
        return booleanF
    }

    void setBooleanF(Boolean booleanF) {
        this.booleanF = booleanF
    }

    Date getDatetimeF() {
        return datetimeF
    }

    void setDatetimeF(Date datetimeF) {
        this.datetimeF = datetimeF
    }

    Date getTimestampF() {
        return timestampF
    }

    void setTimestampF(Date timestampF) {
        this.timestampF = timestampF
    }

    Date getDateF() {
        return dateF
    }

    void setDateF(Date dateF) {
        this.dateF = dateF
    }

    Date getTimeF() {
        return timeF
    }

    void setTimeF(Date timeF) {
        this.timeF = timeF
    }

    String getCharF() {
        return charF
    }

    void setCharF(String charF) {
        this.charF = charF
    }

    String getVarcharF() {
        return varcharF
    }

    void setVarcharF(String varcharF) {
        this.varcharF = varcharF
    }

    String getLongvarcharF() {
        return longvarcharF
    }

    void setLongvarcharF(String longvarcharF) {
        this.longvarcharF = longvarcharF
    }

    UUID getUuidF() {
        return uuidF
    }

    void setUuidF(UUID uuidF) {
        this.uuidF = uuidF
    }

    String getMismatchedName() {
        return mismatchedName
    }

    void setMismatchedName(String mismatchedName) {
        this.mismatchedName = mismatchedName
    }
}
