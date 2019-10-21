package com.github.haflife3.datazilla.dialect.operator;

import java.util.Arrays;
import java.util.List;

public class PgOprStore implements OprStore {
    @Override
    public List<String> getSingularOperators() {
        return Arrays.asList("is null","is not null");
    }

    @Override
    public List<String> getBinaryOperators() {
        return Arrays.asList("<>","SIMILAR TO","~","~*","!~","!~*");
    }
}
