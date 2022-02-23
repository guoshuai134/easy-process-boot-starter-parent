package com.shallow.universe.process.core.annotation;

import java.lang.annotation.*;

/**
 * ClassName:
 * Description: TODO
 *
 * @author: Guo Shuai
 * @version: 1.0
 * @Date: 2021\12\31 0031
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrimaryKey {

    boolean underscoreToCamelCase() default false;
}
