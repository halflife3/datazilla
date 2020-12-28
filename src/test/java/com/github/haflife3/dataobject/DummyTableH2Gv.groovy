package com.github.haflife3.dataobject

import com.github.haflife3.datazilla.annotation.Table
import com.github.haflife3.datazilla.annotation.Column
import uk.co.jemos.podam.common.*

@Table("DUMMY_TABLE")
class DummyTableH2Gv extends DummyTable implements Serializable{

    /**  */
    @PodamExclude
    @Column("ID")
    private Long id

    /**  */
    @PodamIntValue(maxValue = 10)
    @Column("INT_F")
    private Integer intF

    /**  */
    @PodamIntValue(maxValue = 50)
    @Column("INTEGER_F")
    private Integer integerF

    /**  */
    @Column("BOOLEAN_F")
    private Boolean booleanF

    /**  */
    @Column("BIT_F")
    private Boolean bitF

    /**  */
    @PodamIntValue(maxValue = 5)
    @Column("TINYINT_F")
    private Byte tinyintF

    /**  */
    @PodamIntValue(maxValue = 10)
    @Column("SMALLINT_F")
    private Short smallintF

    /**  */
    @PodamIntValue(minValue = 2000,maxValue = 2090)
    @Column("YEAR_F")
    private Short yearF

    /**  */
    @Column("DECIMAL_F")
    private BigDecimal decimalF

    /**  */
    @PodamDoubleValue(maxValue = 999.0D)
    @Column("DOUBLE_F")
    private Double doubleF

    /**  */
    @Column("TIME_F")
    private Date timeF

    /**  */
    @Column("DATE_F")
    private Date dateF

    /**  */
    @Column("TIMESTAMP_F")
    private Date timestampF

    /**  */
    @Column("DATETIME_F")
    private Date datetimeF

    /**  */
    @PodamStringValue(length = 5)
    @Column("VARCHAR_F")
    private String varcharF

    @PodamStringValue(length = 5)
    @Column("name_mismatch_f")
    private String mismatchedName

    /**  */
    @PodamCharValue
    @Column("CHAR_F")
    private String charF

    /**  */
    @Column("UUID_F")
    private UUID uuidF

    Long getId() {
        return id
    }

    void setId(Long id) {
        this.id = id
    }

    Integer getIntF() {
        return intF
    }

    void setIntF(Integer intF) {
        this.intF = intF
    }

    Integer getIntegerF() {
        return integerF
    }

    void setIntegerF(Integer integerF) {
        this.integerF = integerF
    }

    Boolean getBooleanF() {
        return booleanF
    }

    void setBooleanF(Boolean booleanF) {
        this.booleanF = booleanF
    }

    Boolean getBitF() {
        return bitF
    }

    void setBitF(Boolean bitF) {
        this.bitF = bitF
    }

    Byte getTinyintF() {
        return tinyintF
    }

    void setTinyintF(Byte tinyintF) {
        this.tinyintF = tinyintF
    }

    Short getSmallintF() {
        return smallintF
    }

    void setSmallintF(Short smallintF) {
        this.smallintF = smallintF
    }

    Short getYearF() {
        return yearF
    }

    void setYearF(Short yearF) {
        this.yearF = yearF
    }

    BigDecimal getDecimalF() {
        return decimalF
    }

    void setDecimalF(BigDecimal decimalF) {
        this.decimalF = decimalF
    }

    Double getDoubleF() {
        return doubleF
    }

    void setDoubleF(Double doubleF) {
        this.doubleF = doubleF
    }

    Date getTimeF() {
        return timeF
    }

    void setTimeF(Date timeF) {
        this.timeF = timeF
    }

    Date getDateF() {
        return dateF
    }

    void setDateF(Date dateF) {
        this.dateF = dateF
    }

    Date getTimestampF() {
        return timestampF
    }

    void setTimestampF(Date timestampF) {
        this.timestampF = timestampF
    }

    Date getDatetimeF() {
        return datetimeF
    }

    void setDatetimeF(Date datetimeF) {
        this.datetimeF = datetimeF
    }

    String getVarcharF() {
        return varcharF
    }

    void setVarcharF(String varcharF) {
        this.varcharF = varcharF
    }

    String getCharF() {
        return charF
    }

    void setCharF(String charF) {
        this.charF = charF
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
