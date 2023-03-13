package org.ohours.userAPI.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private long startTime = 0L;

    private boolean counting = false;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
    }

    @Around("springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        startTime = System.nanoTime();
        counting = true;
        Object result = joinPoint.proceed();
        long endTime = System.nanoTime();
        if (log.isDebugEnabled()) {
            log.debug("Exit: {}.{}() with result = {} and duration = {}ms", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), result, TimeUnit.NANOSECONDS.toMillis(endTime - startTime));
            counting = false;
        }
        return result;
    }

    @AfterThrowing(pointcut = "springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        long endTime = System.nanoTime();
        String timer = (counting ? String.format(" and duration = %dms", TimeUnit.NANOSECONDS.toMillis(endTime - startTime)) : "");
        log.error("Exception in {}.{}() with cause = {}" + timer, joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause().getLocalizedMessage() : e.getLocalizedMessage());
        counting = false;
    }

}
