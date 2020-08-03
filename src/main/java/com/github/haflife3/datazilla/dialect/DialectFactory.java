package com.github.haflife3.datazilla.dialect;


import java.util.Arrays;
import java.util.List;

import static com.github.haflife3.datazilla.dialect.DialectConst.*;


public class DialectFactory {
    public static List<String> SUPPORTED_DB = Arrays.asList(MYSQL,PG,H2,SQLITE,HSQLDB,MSSQL);
}
