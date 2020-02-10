package com.github.haflife3.datazilla.pojo;

import java.util.Map;

public class Table2JavaMeta {
    private String driver;
    private String dbSchema;
    private String connectParams;
    private String dbUrl;
    private String dbUser;
    private String dbPass;
    private String srcRoot;
    private String domainPackage;
    private boolean lombokMode;
    private boolean autoColumnDetection;
    private Map<String,String> table2ClassMap;
    private Map<String,String> extraTypeMap;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDbSchema() {
        return dbSchema;
    }

    public void setDbSchema(String dbSchema) {
        this.dbSchema = dbSchema;
    }

    public String getConnectParams() {
        return connectParams;
    }

    public void setConnectParams(String connectParams) {
        this.connectParams = connectParams;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public String getSrcRoot() {
        return srcRoot;
    }

    public void setSrcRoot(String srcRoot) {
        this.srcRoot = srcRoot;
    }

    public String getDomainPackage() {
        return domainPackage;
    }

    public void setDomainPackage(String domainPackage) {
        this.domainPackage = domainPackage;
    }

    public Map<String, String> getTable2ClassMap() {
        return table2ClassMap;
    }

    public boolean isLombokMode() {
        return lombokMode;
    }

    public void setLombokMode(boolean lombokMode) {
        this.lombokMode = lombokMode;
    }

    public boolean isAutoColumnDetection() {
        return autoColumnDetection;
    }

    public void setAutoColumnDetection(boolean autoColumnDetection) {
        this.autoColumnDetection = autoColumnDetection;
    }

    public void setTable2ClassMap(Map<String, String> table2ClassMap) {
        this.table2ClassMap = table2ClassMap;
    }

    public Map<String, String> getExtraTypeMap() {
        return extraTypeMap;
    }

    public void setExtraTypeMap(Map<String, String> extraTypeMap) {
        this.extraTypeMap = extraTypeMap;
    }
}
