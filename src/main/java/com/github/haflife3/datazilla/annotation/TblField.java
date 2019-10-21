package com.github.haflife3.datazilla.annotation;

import java.lang.annotation.*;

/**
 * @author halflife3
 * @date 2019/6/19
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TblField {
    /**
     * As the canonical table field name, the bean field name will apply should this value be empty.
     */
    String value() default "";
    /**
     * Used to compose sql, the above value(or bean field name if value itself is empty) will be used if empty.
     */
    String customField() default "";
}
