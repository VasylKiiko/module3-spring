package com.example.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@Aspect
public class LoggingAspect {

    @Pointcut("@within(org.springframework.stereotype.Repository)")
    public void logRepositoryMethod() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void logServiceMethod() {
    }

    @Around("logServiceMethod()")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Start of method: {}", joinPoint.getSignature().getName());
        var arguments = joinPoint.getArgs();
        if (arguments != null && arguments.length > 0) {
            var args = Stream.of(arguments).map(Object::toString).collect(Collectors.joining(", "));
            log.info("args: {}", args);
        }
        var result =  joinPoint.proceed();

        log.info("End of method {}", joinPoint.getSignature().getName());
        return result;
    }
}
