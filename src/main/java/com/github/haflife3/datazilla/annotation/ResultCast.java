package com.github.haflife3.datazilla.annotation;

import com.github.haflife3.datazilla.enums.ResultCastStrategy;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResultCast {
    ResultCastStrategy value() default ResultCastStrategy.DEFAULT;
}
