package com.github.haflife3.datazilla.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CondOpr {
    String value() default "=";
    String columnName() default "";
}
