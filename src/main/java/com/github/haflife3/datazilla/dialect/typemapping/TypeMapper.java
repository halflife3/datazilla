package com.github.haflife3.datazilla.dialect.typemapping;

import com.github.haflife3.datazilla.dialect.DatabaseTypeMatcher;

import java.util.Map;

public interface TypeMapper extends DatabaseTypeMatcher {
    Map<String,String> getTypeMap();
}
