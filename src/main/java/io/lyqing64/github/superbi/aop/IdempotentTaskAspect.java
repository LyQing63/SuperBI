package io.lyqing64.github.superbi.aop;

import io.lyqing64.github.superbi.annotations.IdempotentTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class IdempotentTaskAspect {

    private final RedisTemplate<String, String> redisTemplate;

    @Around("@annotation(idempotentTask)")
    public Object around(ProceedingJoinPoint joinPoint, IdempotentTask idempotentTask) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();

        // 使用 SpEL 解析幂等 key
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        ExpressionParser parser = new SpelExpressionParser();
        String redisKey = "idempotent:" + parser.parseExpression(idempotentTask.key()).getValue(context, String.class);

        Boolean exists = redisTemplate.hasKey(redisKey);
        if (Boolean.TRUE.equals(exists)) {
            log.warn("重复任务拦截 [{}]，直接跳过", redisKey);
            return null;
        }

        try {
            Object result = joinPoint.proceed();
            redisTemplate.opsForValue().set(redisKey, "1", Duration.ofSeconds(idempotentTask.timeoutSeconds()));
            return result;
        } catch (Exception e) {
            log.error("任务处理失败：{}", redisKey, e);
            throw e;
        }
    }
}