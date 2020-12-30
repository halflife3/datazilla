package com.github.haflife3.datazilla.pojo;

import java.util.List;

@FunctionalInterface
public interface CondCrafter<E> {
    List<Cond> craft(E e);
}
