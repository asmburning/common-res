package org.lxy.config;


import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectionFilter {
  String name() default Constant.PROJECTION_SYMBOL;
}
