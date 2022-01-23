package com.f4n.blog.common.cache;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    /* 缓存的过期时间*/
    long expire() default 1 * 60 * 1000;
    /* 缓存的标识 key */
    String name() default "";
}
