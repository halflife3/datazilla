package com.github.haflife3.datazilla.dialect.operator;

import java.util.ArrayList;
import java.util.List;

public class DefaultOprStore implements OprStore {
    @Override
    public List<String> getSingularOperators() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getBinaryOperators() {
        return new ArrayList<>();
    }
}
