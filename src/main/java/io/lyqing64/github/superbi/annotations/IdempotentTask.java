package io.lyqing64.github.superbi.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface IdempotentTask {
    /**
     * SpEL 表达式，用于提取幂等 key，例如 "#task.id"
     */
    String key();

    /**
     * 幂等键过期时间（秒），默认2小时
     */
    int timeoutSeconds() default 7200;
}