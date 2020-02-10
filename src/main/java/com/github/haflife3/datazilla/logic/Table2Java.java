package com.github.haflife3.datazilla.logic;

import com.github.haflife3.datazilla.annotation.Table;
import com.github.haflife3.datazilla.annotation.TblField;
import com.github.haflife3.datazilla.dialect.DialectFactory;
import com.github.haflife3.datazilla.misc.DBException;
import com.github.haflife3.datazilla.misc.PlatformUtils;
import com.github.haflife3.datazilla.pojo.Table2JavaMeta;
import com.google.common.base.CaseFormat;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Table2Java {
    public static void main(String[] args) {
        try {
            initMeta((args!=null&&args.length>0)?args[0]:null);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String dbType;
    private static Table2JavaMeta meta;

    private static final Map<String,String> TYPE_MAP = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private static void initMeta(String metaFilePath)throws Exception{
        if(StringUtils.isBlank(metaFilePath)){
            metaFilePath = "Table2JavaMeta.json";
        }
        File metaFile = new File(metaFilePath);
        if(metaFile.exists()){
            meta = new Gson().fromJson(FileUtils.readFileToString(metaFile,"utf-8"), Table2JavaMeta.class);
            dbType = new PlatformUtils().determineDatabaseType(meta.getDriver(), meta.getDbUrl());
            TYPE_MAP.putAll(DialectFactory.getTypeMapper(dbType).getTypeMap());
            Map<String, String> table2ClassMap = meta.getTable2ClassMap();
            for(String key:table2ClassMap.keySet()){
                String value = table2ClassMap.get(key);
                if(StringUtils.isBlank(value)){
                    throw new DBException("Java bean name for "+key+" is empty!");
                }
            }
            Map<String, String> extraTypeMap = meta.getExtraTypeMap();
            if(MapUtils.isNotEmpty(extraTypeMap)){
                TYPE_MAP.putAll(extraTypeMap);
            }
        }else {
            throw new Exception("metaFilePath:"+metaFilePath+" dosen't exist!");
        }
    }

    private static void init(){
        String url = meta.getDbUrl();
        if(meta.getDbSchema()!=null&& !"".equals(meta.getDbSchema())){
            url += "/"+meta.getDbSchema();
        }
        if(meta.getConnectParams()!=null&& !"".equals(meta.getConnectParams())){
            url += "?"+meta.getConnectParams();
        }
        try (Connection conn = DriverManager.getConnection(url, meta.getDbUser(), meta.getDbPass())) {
            Class.forName(meta.getDriver());
            initDir();
//            initDomainParent();
            initDomains(conn);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static String getFullDir(){
        String domainPackageDir = meta.getDomainPackage().replaceAll("\\.","/");
        return new File("./").getAbsolutePath()+meta.getSrcRoot() +"/"+domainPackageDir;
    }

    private static void initDir() throws Exception {
        String fullDir = getFullDir();
        System.out.println("making dir:"+fullDir);
        FileUtils.forceMkdir(new File(fullDir));
    }

    private static void initDomains(Connection conn)throws Exception{
        String fullDir = getFullDir();
        DatabaseMetaData md = conn.getMetaData();
        Map<String,String> table2ClassMap = new TreeMap<>();
        if(MapUtils.isEmpty(meta.getTable2ClassMap())) {
            return;
        }else {
            table2ClassMap = meta.getTable2ClassMap();
        }
        table2ClassMap.forEach((tableName, className) -> {
            try {
                String beanContent = getBeanContentFromTable(md, tableName, className);
                String javaFileName = fullDir+"/"+className+".java";
                File javaFile = new File(javaFileName);
                boolean fileExist = javaFile.exists();
                boolean needGen = true;

                if(fileExist){
                    String fileContent = FileUtils.readFileToString(javaFile, UTF_8);
                    if(fileContent.equals(beanContent)){
                        needGen = false;
                    }
                }

                if(needGen) {
                    FileUtils.writeStringToFile(javaFile, beanContent, "utf-8", false);
                    System.out.println(" (" + (fileExist ? "updated" : "*created") + ") " + tableName + " --> " + className);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static String getBeanContentFromTable(DatabaseMetaData md ,String tableName,String className) throws Exception {
        String tableComment = getTableComment(md, tableName);
        String beanContent = "";
        String packagePart = "package "+meta.getDomainPackage()+";\n";
        List<ColumnMeta> metas = getColumnMeta(md,tableName);
        Set<String> importPartSet = new LinkedHashSet<>();
        if(meta.isLombokMode()){
            importPartSet.add("lombok.Data");
        }
        String importPart = "import "+ Table.class.getCanonicalName()+";\n";
        if(!meta.isAutoColumnDetection()) {
            importPart += "import " + TblField.class.getCanonicalName() + ";\n";
        }
        String fieldsPart = "";
        String getSetPart = "";

        String noArgConstructor = "  public "+className+"(){\n  }\n";
        String builderConstructor = "  private "+className+"(Builder builder) {\n";
        String builderClass = "  public static final class Builder {\n";
        String builderFieldsPart = "";
        String builderGetSetPart = "";

        for (ColumnMeta columnMeta : metas) {
            String name = columnMeta.getName();
            String javaVarName = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, name.toUpperCase());
            String type = columnMeta.getType();
            String comment = columnMeta.getComment();
            String javaType = TYPE_MAP.get(type.toUpperCase().replaceAll("\\s+"," ").replaceAll("UNSIGNED",""));
            if(StringUtils.isBlank(javaType)){
                throw new DBException("type:"+type+" has no corresponding java type!");
            }
            String simpleJavaType = getSimpleJavaType(javaType);
            if(javaType.contains(".")){
                importPartSet.add(javaType);
            }
//            if(javaType.equals("java.util.Date")){
//                importPartSet.add("com.fasterxml.jackson.annotation.JsonFormat");
//                if(type.equalsIgnoreCase("DATE")) {
//                    fieldsPart += "  @JsonFormat(pattern=\"yyyy-MM-dd\",timezone = \"GMT+8\")\n";
//                }else {
//                    fieldsPart += "  @JsonFormat(pattern=\"yyyy-MM-dd HH:mm:ss\",timezone = \"GMT+8\")\n";
//                }
//            }
            fieldsPart += "\n";
            if(StringUtils.isNotBlank(comment)) {
                fieldsPart += "  /** " + comment + " */\n";
            }
            if(!meta.isAutoColumnDetection()) {
                fieldsPart += "  @TblField(\"" + name + "\")\n";
            }
            fieldsPart += "  private "+simpleJavaType+" "+javaVarName+";\n";
            builderFieldsPart += "    private "+simpleJavaType+" "+javaVarName+";\n";
            getSetPart += "  public "+simpleJavaType+" get"+ StringUtils.capitalize(javaVarName)+"() {\n    return "+javaVarName+";\n  }\n\n";
            getSetPart += "  public void set"+ StringUtils.capitalize(javaVarName)+"( "+simpleJavaType+" "+javaVarName+" ) {\n    this."+javaVarName+" = "+javaVarName+";\n  }\n\n";

            builderGetSetPart+="    public Builder "+javaVarName+"("+simpleJavaType+" "+javaVarName+") {\n";
            builderGetSetPart+="      this."+javaVarName+" = "+javaVarName+";\n";
            builderGetSetPart+="      return this;\n";
            builderGetSetPart+="    }\n\n";

            builderConstructor+="    set"+ StringUtils.capitalize(javaVarName)+"(builder."+javaVarName+");\n";
        }
        builderConstructor +="  }\n\n";

        builderClass += builderFieldsPart+"\n";
        builderClass += "    public Builder() {}\n\n";
        builderClass += builderGetSetPart;
        builderClass += "    public "+className+" build() {\n";
        builderClass += "      return new "+className+"(this);\n";
        builderClass += "    }\n";
        builderClass += "  }\n";

        for (String importType : importPartSet) {
            importPart += "import "+importType+";\n";
        }

//        String tableNamePart ="  public String fetchTableName(){\n";
//        tableNamePart+="    return \""+tableName+"\";\n";
//        tableNamePart+="  }\n";

        beanContent += packagePart+"\n";
        beanContent += importPart+"\n";
//        beanContent += "// << ALERT >> : DO NOT MODIFY THIS FILE !!! \n";
        if(StringUtils.isNotBlank(tableComment)){
            beanContent += "/** "+tableComment+" */\n";
        }
        if(meta.isAutoColumnDetection()) {
            beanContent += "@Table(value = \"" + tableName + "\", autoColumnDetection = true)\n";
        }else {
            beanContent += "@Table(\"" + tableName + "\")\n";
        }
        if(meta.isLombokMode()){
            beanContent += "@Data\n";
        }
        beanContent += "public class "+className+" {\n";
        beanContent += fieldsPart+"\n";
        if(!meta.isLombokMode()) {
            beanContent += noArgConstructor + "\n";
            beanContent += builderConstructor + "\n";
            beanContent += getSetPart;
            beanContent += builderClass;
        }
//        beanContent += tableNamePart;
        beanContent += "}";


        return beanContent;
    }

    private static String getSimpleJavaType(String javaType){
        String simpleJavaType = javaType;
        if(javaType.contains(".")){
            String[] javaTypeArr = javaType.split("\\.");
            int length = javaTypeArr.length;
            simpleJavaType = javaTypeArr[length-1];
        }
        return simpleJavaType;
    }

    private static String getTableComment(DatabaseMetaData md , String table) throws Exception {
        String comment = "";
        table = DialectFactory.getEntityRegulator(dbType).simpleTable(table);
        ResultSet resultSet = md.getTables(null, null, table, null);
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            comment = resultSet.getString("REMARKS");
            if(tableName.equalsIgnoreCase(table)){
                break;
            }
        }
        return comment;
    }


    private static List<ColumnMeta> getColumnMeta(DatabaseMetaData md , String table) throws Exception {
        List<ColumnMeta> metas = new ArrayList<>();
        table = DialectFactory.getEntityRegulator(dbType).simpleTable(table);
        ResultSet resultSet = md.getColumns(null, null, table, null);
        while (resultSet.next()) {
            String name = resultSet.getString("COLUMN_NAME");
            String type = resultSet.getString("TYPE_NAME").toUpperCase();
            String comment = resultSet.getString("REMARKS");
            metas.add(new ColumnMeta(name,type,comment));
        }
        if(CollectionUtils.isEmpty(metas)){
            throw new DBException("Table "+table+" doesn't exist!");
        }
        return metas;
    }


    private static class ColumnMeta{
        private String name;
        private String type;
        private String comment;

        public ColumnMeta() {
        }

        public ColumnMeta(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public ColumnMeta(String name, String type, String comment) {
            this.name = name;
            this.type = type;
            this.comment = comment;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
