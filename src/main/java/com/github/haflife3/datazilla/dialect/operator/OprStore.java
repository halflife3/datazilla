package com.github.haflife3.datazilla.dialect.operator;

import org.apache.commons.collections.CollectionUtils;

import java.util.*;

public interface OprStore {
    List<String> baseOperators = Arrays.asList("=","!=",">",">=","<=","<","like","not like","in","not in");
    List<String> getSingularOperators();
    List<String> getBinaryOperators();
    default List<String> getAllOperators(){
        Set<String> all = new HashSet<>(baseOperators);
        if(CollectionUtils.isNotEmpty(getSingularOperators())){
            all.addAll(getSingularOperators());
        }
        if(CollectionUtils.isNotEmpty(getBinaryOperators())){
            all.addAll(getBinaryOperators());
        }
        return new ArrayList<>(all);
    }
}
