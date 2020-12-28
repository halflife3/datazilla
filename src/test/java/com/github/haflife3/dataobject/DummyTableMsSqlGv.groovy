package com.github.haflife3.dataobject

import com.github.haflife3.datazilla.annotation.Table
import com.github.haflife3.datazilla.annotation.Column
import uk.co.jemos.podam.common.PodamBooleanValue
import uk.co.jemos.podam.common.PodamCharValue
import uk.co.jemos.podam.common.PodamDoubleValue
import uk.co.jemos.podam.common.PodamExclude
import uk.co.jemos.podam.common.PodamIntValue
import uk.co.jemos.podam.common.PodamShortValue
import uk.co.jemos.podam.common.PodamStringValue

@Table("dummy_table")
class DummyTableMsSqlGv extends DummyTable implements Serializable{

    @PodamExclude
    @Column("id")
    private Long id

    @PodamBooleanValue
    @Column("bit_f")
    private Boolean bitF

    @PodamCharValue
    @Column("char_f")
    private String charF

    @Column("date_f")
    private Date dateF

    @Column("datetime_f")
    private Date datetimeF

    @PodamDoubleValue(maxValue = 9999.0D)
    @Column("decimal_f")
    private Double decimalF

    @PodamDoubleValue(maxValue = 99.0D)
    @Column("float_f")
    private Double floatF

    @PodamIntValue(minValue = 1,maxValue = 10000)
    @Column("int_f")
    private Integer intF

    @PodamStringValue(length = 5)
    @Column("nchar_f")
    private String ncharF

    @PodamStringValue(length = 10)
    @Column("ntext_f")
    private String ntextF

    @PodamDoubleValue(maxValue = 9999.0D)
    @Column("numeric_f")
    private Double numericF

    @PodamStringValue(length = 10)
    @Column("nvarchar_f")
    private String nvarcharF

    @PodamDoubleValue(maxValue = 9999.0D)
    @Column("real_f")
    private Double realF

    @Column("smalldatetime_f")
    private Date smalldatetimeF

    @PodamShortValue
    @Column("smallint_f")
    private Short smallintF

    @PodamStringValue(length = 100)
    @Column("text_f")
    private String textF

    @PodamShortValue
    @Column("tinyint_f")
    private Short tinyintF

    @PodamStringValue(length = 15)
    @Column("varchar_f")
    private String varcharF

    @PodamStringValue(length = 5)
    @Column("name_mismatch_f")
    private String mismatchedName

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Boolean getBitF() {
        return bitF
    }

    void setBitF(Boolean bitF) {
        this.bitF = bitF
    }

    String getCharF() {
        return charF
    }

    void setCharF(String charF) {
        this.charF = charF
    }

    Date getDateF() {
        return dateF
    }

    void setDateF(Date dateF) {
        this.dateF = dateF
    }

    Date getDatetimeF() {
        return datetimeF
    }

    void setDatetimeF(Date datetimeF) {
        this.datetimeF = datetimeF
    }

    Double getDecimalF() {
        return decimalF
    }

    void setDecimalF(Double decimalF) {
        this.decimalF = decimalF
    }

    Double getFloatF() {
        return floatF
    }

    void setFloatF(Double floatF) {
        this.floatF = floatF
    }

    Integer getIntF() {
        return intF
    }

    void setIntF(Integer intF) {
        this.intF = intF
    }

    String getNcharF() {
        return ncharF
    }

    void setNcharF(String ncharF) {
        this.ncharF = ncharF
    }

    String getNtextF() {
        return ntextF
    }

    void setNtextF(String ntextF) {
        this.ntextF = ntextF
    }

    Double getNumericF() {
        return numericF
    }

    void setNumericF(Double numericF) {
        this.numericF = numericF
    }

    String getNvarcharF() {
        return nvarcharF
    }

    void setNvarcharF(String nvarcharF) {
        this.nvarcharF = nvarcharF
    }

    Double getRealF() {
        return realF
    }

    void setRealF(Double realF) {
        this.realF = realF
    }

    Date getSmalldatetimeF() {
        return smalldatetimeF
    }

    void setSmalldatetimeF(Date smalldatetimeF) {
        this.smalldatetimeF = smalldatetimeF
    }

    Short getSmallintF() {
        return smallintF
    }

    void setSmallintF(Short smallintF) {
        this.smallintF = smallintF
    }

    String getTextF() {
        return textF
    }

    void setTextF(String textF) {
        this.textF = textF
    }

    Short getTinyintF() {
        return tinyintF
    }

    void setTinyintF(Short tinyintF) {
        this.tinyintF = tinyintF
    }

    String getVarcharF() {
        return varcharF
    }

    void setVarcharF(String varcharF) {
        this.varcharF = varcharF
    }

    String getMismatchedName() {
        return mismatchedName
    }

    void setMismatchedName(String mismatchedName) {
        this.mismatchedName = mismatchedName
    }
}
